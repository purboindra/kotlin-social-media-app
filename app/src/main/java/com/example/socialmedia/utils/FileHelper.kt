package com.example.socialmedia.utils

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraExecutor
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
    
    fun captureVideo(
        onVideoSaved: (Uri) -> Unit,
        onDurationUpdate: (Long) -> Unit,
        context: Context,
        videoCapture: VideoCapture<Recorder>?,
        recording: Recording?,
    ): Recording? {
        
        Log.d(
            "VideoCapture",
            "Capturing video: ${videoCapture == null} ${recording == null}"
        )
        
        if (videoCapture == null) return null
        
        if (recording != null) {
            recording.stop()
            return null
        }
        
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())
        
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Video.Media.RELATIVE_PATH,
                    "Movies/CameraX-Video"
                )
            }
        }
        
        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()
        
        val startTime = System.currentTimeMillis()
        
        val newRecording = videoCapture.output.prepareRecording(
            context,
            mediaStoreOutputOptions
        ).apply {
            if (PermissionChecker.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                withAudioEnabled()
            }
        }.start(
            ContextCompat.getMainExecutor(context)
        ) { recordEvent ->
            Log.d("VideoCapture", "Video capture output event: $recordEvent")
            when (recordEvent) {
                is VideoRecordEvent.Start -> {
                    Toast.makeText(
                        context,
                        "Recording Started",
                        Toast.LENGTH_LONG
                    ).show()
                }
                
                is VideoRecordEvent.Status -> {
                    val durationMills = System.currentTimeMillis() - startTime
                    onDurationUpdate(durationMills / 1000)
                }
                
                is VideoRecordEvent.Finalize -> {
                    if (!recordEvent.hasError()) {
                        val savedUri =
                            recordEvent.outputResults.outputUri
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
                        Log.e(
                            "VideoCapture",
                            "Video capture failed: ${recordEvent.error}"
                        )
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