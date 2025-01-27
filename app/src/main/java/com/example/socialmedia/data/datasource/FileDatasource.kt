package com.example.socialmedia.data.datasource

import android.content.Context
import android.net.Uri
import com.example.socialmedia.data.model.UploadImageModel

interface FileDatasource {
    suspend fun uploadImage(imageByte: ByteArray): Result<UploadImageModel?>
}