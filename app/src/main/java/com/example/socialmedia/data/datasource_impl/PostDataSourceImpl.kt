package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreatePostModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import java.util.UUID

class PostDataSourceImpl(
    private val supabase: SupabaseClient,
    private val datastore: AppDataStore
) : PostDatasource {
    override suspend fun createPost(
        imageKey: String,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean> {
        Log.d("PostDataSourceImpl", "Creating post")
        try {
            val userId =
                datastore.userId.first() ?: throw Exception("User ID not found")
            
            val uuid = UUID.randomUUID().toString()
            
            val createPostModel = CreatePostModel(
                id = uuid,
                imageKey = imageKey,
                caption = caption,
                taggedUsers = taggedUsers,
                taggedLocation = taggedLocation,
                userId,
            )
            
            supabase.from("posts").insert(createPostModel)
            
            return Result.success(true)
            
        } catch (e: Exception) {
            throw e
        }
    }
}