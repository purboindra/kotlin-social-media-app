package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreatePostModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

class PostDataSourceImpl(
    private val supabase: SupabaseClient,
    private val datastore: AppDataStore
) : PostDatasource {
    override suspend fun createPost(
        image: UploadImageModel,
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
                imageKey = image.key,
                imagePath = image.path,
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
    
    override suspend fun fetchAllPosts(): Result<List<PostModel>> {
        try {
            val columns = Columns.raw(
                """
    id,
    created_at,
    caption,
    user (
      id,
      full_name,
      profile_picture
    ),
    likes,
    image_key,
    image_path,
    tagged_location,
    tagged_users
    """.trimIndent()
            )
            
            val rawPost = supabase.from("posts").select(
                columns
            )
            val posts = rawPost.data
            val decodePost = Json.decodeFromString<List<PostModel>>(posts)
            
            val bucket = supabase.storage.from("posts")
            
            val updatedPost = decodePost.map { post ->
                
                val url = try {
                    bucket.createSignedUrl(
                        path = post.imagePath,
                        expiresIn = 5.minutes
                    )
                } catch (e: RestException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url RestException",
                        e
                    )
                    throw e
                } catch (e: HttpRequestException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url HttpRequestException",
                        e
                    )
                    throw e
                } catch (e: HttpRequestTimeoutException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url HttpRequestTimeoutException",
                        e
                    )
                    throw e
                } catch (e: Exception) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url Exception",
                        e
                    )
                    throw e
                }
                
                post.copy(imageUrl = url)
            }
            
            return Result.success(updatedPost)
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error fetching all posts", e)
            throw e
        }
    }
}