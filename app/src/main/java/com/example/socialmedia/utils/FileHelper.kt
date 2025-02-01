package com.example.socialmedia.utils

import android.content.ContentResolver
import android.graphics.Camera
import android.net.Uri
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraExecutor
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import java.io.File
import java.io.InputStream
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
    
    fun takePicture(
        imageCapture: ImageCapture,
        onSave: (Uri) -> Unit,
        onError: (Exception) -> Unit
    ) {
        
        val imageDate = System.currentTimeMillis()
        
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            File(
                "${imageDate}.jpg"
            )
        ).build()
        
        val cameraExecutor: ExecutorService =
            Executors.newSingleThreadExecutor()
        
        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor,
            
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                    cameraExecutor.shutdown()
                }
                
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        onSave(it)
                    }
                    cameraExecutor.shutdown()
                }
            }
        )
    }
    
}