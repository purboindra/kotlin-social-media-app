package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreateCommentModel
import com.example.socialmedia.data.model.CreatePostModel
import com.example.socialmedia.data.model.CreateSavePostModel
import com.example.socialmedia.data.model.LikeModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.SavePostResult
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
    image_key,
    image_path,
    tagged_location,
    tagged_users,
    comments (
          id,
          comment,
          user(
             id,
      full_name,
      profile_picture
          ),
          post_id,
          created_at
    )
    """.trimIndent()
            )
            
            val rawPost = supabase.from("posts").select(
                columns
            ) {
                order("created_at", Order.DESCENDING)
            }
            
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
            
            val columnsLikes = Columns.list("user_id", "post_id")
            
            val userId = datastore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val fetchLikes = supabase.from("likes").select(columnsLikes) {
                filter {
                    and {
                        eq("user_id", userId)
                    }
                }
            }
            
            val dataLikes = fetchLikes.data
            
            val decodeLikes = Json.decodeFromString<List<LikeModel>>(dataLikes)
            
            val likeMap = decodeLikes.associateBy {
                it.postId
            }
            
            val modifiedPosts = updatedPost.map { post ->
                post.copy(
                    hasLike = likeMap.containsKey(post.id)
                )
            }
            
            return Result.success(modifiedPosts)
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error fetching all posts", e)
            throw e
        }
    }
    
    override suspend fun createLike(id: String): Result<Boolean> {
        try {
            val userId =
                datastore.userId.first() ?: throw Exception("User ID not found")
            
            val like = LikeModel(
                userId = userId,
                postId = id,
            )
            
            supabase.from("likes").insert(like)
            
            return Result.success(true)
            
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error create like", e)
            throw e
        }
    }
    
    override suspend fun deleteLike(id: String): Result<Boolean> {
        try {
            
            datastore.userId.first() ?: throw Exception("User ID not found")
            
            supabase.from("likes").delete {
                filter {
                    eq("post_id", id)
                }
            }
            
            return Result.success(true)
            
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error delete like", e)
            throw e
        }
    }
    
    override suspend fun fetchAllLikes(): Result<List<LikeModel>> {
        try {
            
            val rawLikes =
                supabase.from("likes").select()
            
            val likes = Json.decodeFromString<List<LikeModel>>(rawLikes.data)
            
            return Result.success(likes)
            
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error fetch all likes", e)
            throw e
        }
    }
    
    override suspend fun createComment(
        id: String,
        comment: String,
    ): Result<Boolean> {
        try {
            
            val userId =
                datastore.userId.first() ?: throw Exception("User ID not found")
            
            val createCommentModel = CreateCommentModel(
                postId = id,
                comment = comment,
                userId = userId,
            )
            
            supabase.from("comments").insert(createCommentModel)
            
            return Result.success(true)
            
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error create comment", e)
            throw e
        }
    }
    
    override suspend fun savedPost(id: String): SavePostResult {
        return try {
            
            val userId = datastore.userId.firstOrNull()
                ?: throw Exception("Invalid credentials")
            
            val post = CreateSavePostModel(
                userId = userId,
                postId = id,
            )
            
            supabase.from("saved_posts").insert(post)
            
            return SavePostResult.Success
            
        } catch (e: Exception) {
            Log.e("PostDataSourceImpl", "Error save post", e)
            SavePostResult.Error(message = e.message ?: "Error saving post")
        }
    }
}