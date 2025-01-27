package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage

class FileDataSourceImpl(
    private val supabase: SupabaseClient,
) : FileDatasource {
    override suspend fun uploadImage(
        imageByte: ByteArray
    ): Result<UploadImageModel?> {
        try {
            val userId = supabase.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User is not authenticated")
            
            Log.d("FileDataSourceImpl", "Uploading image for user $userId")
            
            val response = supabase.storage
                .from("posts")
                .upload(
                    "$userId-${System.currentTimeMillis()}.jpg",
                    imageByte
                )
            
            Log.d("FileDataSourceImpl", "Upload response: $response")
            
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
    
}