package com.example.socialmedia.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

object PostHelper {
    
    fun scanMediaFile(context: Context, uri: Uri) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(uri.path),
            arrayOf("image/*"),
        ) { path, uriItem ->
            Log.d(
                "scanMediaFile",
                "Scanned path: $path, uriItem:$uriItem, uri: $uri"
            )
        }
    }
    
    @SuppressLint("Recycle")
    fun getGalleryImages(context: Context): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        
        query?.use { cursor ->
            Log.d("getGalleryImages", "Cursor count: ${cursor.count}")
            
            if (cursor.count == 0) {
                Log.e("getGalleryImages", "No images found in gallery")
                return emptyList()
            }
            
            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageUris.add(contentUri)
            }
        }
        
        Log.d("getGalleryImages", "Getting gallery images: $imageUris")
        
        return imageUris
    }
}