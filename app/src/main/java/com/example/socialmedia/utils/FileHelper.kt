package com.example.socialmedia.utils

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.MirrorMode
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume

object FileHelper {
    
    fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use {
                it.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun takeVideo(
        context: Context,
        onSave: (Uri) -> Unit,
        onError: (Exception) -> Unit,
        lifecycleOwner: androidx.lifecycle.LifecycleOwner,
        cameraPreviewUseCase: Preview
    ): CameraControl {
        
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(context)
        
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )
        
        val recorder = Recorder.Builder().setQualitySelector(
            qualitySelector
        ).build()
        
        val videoCapture = VideoCapture.Builder(recorder).setMirrorMode(
            MirrorMode.MIRROR_MODE_ON_FRONT_ONLY
        ).build()
        
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector, videoCapture,
            cameraPreviewUseCase,
        )
        
        startRecording(
            context,
            videoCapture,
            onSave,
            onError,
        )
        
        return camera.cameraControl
    }
    
    private suspend fun startRecording(
        context: Context,
        videoCapture: VideoCapture<Recorder>,
        onSave: (Uri) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis()) + ".mp4"
        
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Video.Media.RELATIVE_PATH,
                    "Movies/CameraX-Video"
                )
            }
        }
        
        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()
        
        try {
            suspendCancellableCoroutine { continuation ->
                val recording = videoCapture.output
                    .prepareRecording(context, mediaStoreOutputOptions)
                    .apply {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {
                            withAudioEnabled()
                        }
                    }
                    .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                        when (recordEvent) {
                            is VideoRecordEvent.Start -> {
                                Log.d("CameraX", "Recording Started")
                            }
                            
                            is VideoRecordEvent.Finalize -> {
                                if (!recordEvent.hasError()) {
                                    val savedUri =
                                        recordEvent.outputResults.outputUri
                                    Log.d("CameraX", "Video saved to $savedUri")
                                    onSave(savedUri)
                                } else {
                                    Log.e(
                                        "CameraX",
                                        "Recording Error: ${recordEvent.error}"
                                    )

//                                    val message = recordEvent.error.m
                                    
                                    onError(Exception("Something went wrong!"))
                                }
                                continuation.resume(Unit)
                            }
                        }
                    }
                
                continuation.invokeOnCancellation {
                    recording.stop()
                }
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
    
    
    fun captureVideo(
        onVideoSaved: (Uri) -> Unit,
        onDurationUpdate: (Long) -> Unit,
        context: Context,
        videoCapture: VideoCapture<Recorder>?,
        recording: Recording?
    ): Recording? {
        if (videoCapture == null) return null
        
        recording?.stop()
        if (recording != null) return null
        
        val videoDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
            "CameraX-Video"
        )
        if (!videoDir.exists()) {
            videoDir.mkdirs()
        }
        
        val videoFile = File.createTempFile(
            SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.US
            ).format(
                System.currentTimeMillis()
            ),
            ".mp4",
            videoDir,
        )
        
        val fileOutputOptions = FileOutputOptions.Builder(
            videoFile
        ).build()
        
        val startTime = System.currentTimeMillis()
        
        val newRecording = videoCapture.output
            .prepareRecording(context, fileOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        Toast.makeText(
                            context,
                            "Recording Started",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    
                    is VideoRecordEvent.Status -> {
                        val durationMillis =
                            System.currentTimeMillis() - startTime
                        val seconds = (durationMillis / 1000).toInt()
                        onDurationUpdate(seconds.toLong())
                    }
                    
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val savedUri = recordEvent.outputResults.outputUri
                            onVideoSaved(savedUri)
                            Toast.makeText(
                                context,
                                "Video saved to $savedUri",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Video capture failed: ${recordEvent.error}",
                                Toast.LENGTH_LONG
                            ).show()
                            return@start
                        }
                    }
                }
            }
        
        return newRecording
    }
    
    
    fun takePicture(
        imageCapture: ImageCapture,
        context: Context,
        onSave: (Uri) -> Unit,
        onError: (Exception) -> Unit
    ) {
        
        val imageDate = System.currentTimeMillis()
        
        val photoFile = File(
            context.externalMediaDirs.firstOrNull(),
            "IMG_${imageDate}.jpg"
        )
        
        val name = SimpleDateFormat(imageDate.toString(), Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/CameraX-Image"
                )
            }
        }
        
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            photoFile,
        ).build()
        
        val cameraExecutor: ExecutorService =
            Executors.newSingleThreadExecutor()
        
        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor,
            
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                    Log.e(
                        "takePicture",
                        "Image capture failed: ${exception.message}"
                    )
                    cameraExecutor.shutdown()
                }
                
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri =
                        outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    onSave(savedUri)
                    cameraExecutor.shutdown()
                }
            }
        )
    }
    
}