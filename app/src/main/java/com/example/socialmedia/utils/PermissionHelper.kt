package com.example.socialmedia.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission

object PermissionHelper {
    fun hasMediaPermissions(context: Context): Boolean {
        val granted =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasImagesPermission = checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_IMAGES
                ) == PermissionChecker.PERMISSION_GRANTED
                
                val hasVideosPermission = checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_VIDEO
                ) == PermissionChecker.PERMISSION_GRANTED
                
                Log.d("Permissions", "READ_MEDIA_IMAGES: $hasImagesPermission")
                Log.d("Permissions", "READ_MEDIA_VIDEO: $hasVideosPermission")
                
                hasImagesPermission && hasVideosPermission
            } else {
                val hasReadPermission = checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PermissionChecker.PERMISSION_GRANTED
                
                Log.d(
                    "Permissions",
                    "READ_EXTERNAL_STORAGE: $hasReadPermission"
                )
                
                hasReadPermission
            }
        
        return granted
    }
    
    
    private const val TAG = "CameraXApp"
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val REQUIRED_PERMISSIONS =
        mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    
}