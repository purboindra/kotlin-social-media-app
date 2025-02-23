package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType

class FileDataSourceImpl(
    private val supabase: SupabaseClient,
) : FileDatasource {
    override suspend fun uploadImage(
        imageByte: ByteArray
    ): Result<UploadImageModel?> {
        try {
            val userId = supabase.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User is not authenticated")
            
            val response = supabase.storage
                .from("posts")
                .upload(
                    "$userId-${System.currentTimeMillis()}.jpg",
                    imageByte
                )
            
            if (response.key.isNullOrEmpty() || response.id
                    .isNullOrEmpty()
            ) throw Exception("Image upload failed")
            
            val uploadImageModel = UploadImageModel(
                key = response.key ?: "",
                id = response.id ?: "",
                path = response.path,
            )
            
            return Result.success(uploadImageModel)
        } catch (e: Exception) {
            Log.e("FileDataSourceImpl", "Error uploading file: ${e.message}")
            return Result.failure(e)
        }
    }
    
    override suspend fun uploadVideo(videoByte: ByteArray): Result<UploadImageModel?> {
        return try {
            val userId = supabase.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User is not authenticated")
            
            val fileName = "$userId-${System.currentTimeMillis()}.mp4"
            val response = supabase.storage.from("instastories").upload(
                fileName,
                videoByte,
                options = {
                    contentType = ContentType("video", "mp4")
                }
            )
            
            if (response.key.isNullOrEmpty() || response.id.isNullOrEmpty()) {
                throw Exception("Video upload failed")
            }
            
            val uploadImageModel = UploadImageModel(
                key = response.key ?: "",
                id = response.id ?: "",
                path = response.path
            )
            
            Result.success(uploadImageModel)
        } catch (e: Exception) {
            Log.e("FileDataSourceImpl", "Error uploading video: ${e.message}")
            Result.failure(e)
        }
    }
    
}