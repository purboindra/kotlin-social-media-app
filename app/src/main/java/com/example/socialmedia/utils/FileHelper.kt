package com.example.socialmedia.utils

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.File
import java.io.FileOutputStream
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
    
    
    suspend fun bindCameraInstaStory(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraPreviewUseCase: Preview
    ): Pair<CameraControl, VideoCapture<Recorder>> {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(context)
        
        processCameraProvider.unbindAll()
        
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(
                Quality.UHD, Quality.FHD, Quality.HD, Quality.SD,
            ),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )
        
        val recorder =
            Recorder.Builder().setQualitySelector(qualitySelector).build()
        
        val videoCapture = VideoCapture.withOutput(recorder)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            videoCapture,
            cameraPreviewUseCase
        )
        
        return Pair(camera.cameraControl, videoCapture)
    }
    
    suspend fun startVideoRecordingInstaStory(
        context: Context,
        videoCapture: VideoCapture<Recorder>,
        onSave: (Uri) -> Unit,
        onError: (Exception) -> Unit,
        onDurationUpdate: (Long) -> Unit,
        onRecordingStop: (() -> Unit)?,
        onRecordingStart: (() -> Unit)?,
    ): Recording? {
        onRecordingStart?.invoke()
        
        val videoFile =
            withContext(Dispatchers.IO) {
                File.createTempFile(
                    "temp_video_", ".mp4"
                )
            }
        
        val outputOptions = FileOutputOptions.Builder(videoFile).build()
        
        val startTime = System.currentTimeMillis()
        
        val recording = videoCapture.output.prepareRecording(
            context,
            outputOptions
        ).apply {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                withAudioEnabled()
            }
        }.start(
            ContextCompat.getMainExecutor(context)
        ) { recordEvent ->
            when (recordEvent) {
                is VideoRecordEvent.Start -> Log.d(
                    "CameraX",
                    "Recording started"
                )
                
                is VideoRecordEvent.Status -> {
                    val durationMills =
                        System.currentTimeMillis() - startTime
                    val seconds = (durationMills / 1000).toInt()
                    onDurationUpdate(seconds.toLong())
                }
                
                is VideoRecordEvent.Finalize -> {
                    if (!recordEvent.hasError()) {
                        val savedUri = recordEvent.outputResults.outputUri
                        onSave(savedUri)
                    } else {
                        Log.e(
                            "CameraX",
                            "Recording Error: ${recordEvent.error}"
                        )
//                            onError(
//                                Exception(
//                                    message = recordEvent.error,
//                                    cause = recordEvent.cause
//                                )
//                            )
                    }
                    onRecordingStart?.invoke()
                }
            }
        }
        
        return recording
        
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
    
    fun testTakePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit,
        context: Context,
        cameraControl: CameraControl
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    
                    onPhotoTaken(rotatedBitmap)
                }
                
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(
                        "CameraX",
                        "Image capture failed: ${exception.message}"
                    )
                }
            }
        )
    }
    
    fun takePicture(
        imageCapture: ImageCapture,
        onSuccess: (Uri) -> Unit,
        onError: (Exception) -> Unit,
        context: Context,
    ) {
        val cameraExecutor: ExecutorService =
            Executors.newSingleThreadExecutor()
        
        imageCapture.takePicture(
            cameraExecutor,
            object : OnImageCapturedCallback() {
                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                    Log.e(
                        "takePicture",
                        "Image capture failed: ${exception.message}"
                    )
                    cameraExecutor.shutdown()
                }
                
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    
                    try {
                        val matrix = Matrix().apply {
                            postRotate(image.imageInfo.rotationDegrees.toFloat())
                        }
                        val rotatedBitmap = Bitmap.createBitmap(
                            image.toBitmap(),
                            0,
                            0,
                            image.width,
                            image.height,
                            matrix,
                            true
                        )
                        val uri = bitmapToUri(context, rotatedBitmap)
                        uri?.let { onSuccess(it) }
                    } catch (e: Exception) {
                        onError(e)
                    }
//                    finally {
//                        image.close()
//                        cameraExecutor.shutdown()
//                    }
                }
                
            }
        )
    }
    
    private fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val file = File(context.cacheDir, "preview_image.jpg")
            file.delete()
            file.createNewFile()
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    
    
}