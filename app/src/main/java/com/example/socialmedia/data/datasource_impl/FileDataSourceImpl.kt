package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage

class FileDataSourceImpl(
    private val supabase: SupabaseClient,
) : FileDatasource {
    override suspend fun uploadImage(
        imageByte: ByteArray
    ): Result<String?> {
        try {
            val userId = supabase.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User is not authenticated")
            
            Log.d("FileDataSourceImpl", "Uploading image for user $userId")
            
            val response = supabase.storage
                .from("users")
                .upload(
                    "posts/$userId-${System.currentTimeMillis()}.jpg",
                    imageByte
                )
            
            Log.d("FileDataSourceImpl", "Upload response: $response")
            return Result.success(response.key)
        } catch (e: Exception) {
            Log.e("FileDataSourceImpl", "Error uploading file: ${e.message}")
            return Result.failure(e)
        }
        
    }
    
}