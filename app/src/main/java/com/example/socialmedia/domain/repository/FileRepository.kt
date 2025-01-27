package com.example.socialmedia.domain.repository

import android.content.Context
import android.net.Uri
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun uploadImage(
        imageByte: ByteArray,
    ): Flow<State<UploadImageModel?>>
}