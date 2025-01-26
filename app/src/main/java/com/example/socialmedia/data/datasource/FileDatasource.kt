package com.example.socialmedia.data.datasource

import android.content.Context
import android.net.Uri

interface FileDatasource {
    suspend fun uploadImage(imageByte: ByteArray): Result<String?>
}