package com.example.socialmedia.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission

object PermissionHelper {
    fun hasMediaPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PermissionChecker.PERMISSION_GRANTED && checkSelfPermission(
                context, android.Manifest.permission.READ_MEDIA_VIDEO
            ) == PermissionChecker.PERMISSION_GRANTED
        } else {
            checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
        }
    }
}