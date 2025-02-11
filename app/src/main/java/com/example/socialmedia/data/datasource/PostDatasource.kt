package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.LikeModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.postgrest.query.Columns

interface PostDatasource {
    suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean>
    
    suspend fun fetchAllPosts(): Result<List<PostModel>>
    suspend fun createLike(id: String): Result<Boolean>
    suspend fun deleteLike(id: String): Result<Boolean>
    suspend fun fetchAllLikes(): Result<List<LikeModel>>
    suspend fun createComment(id: String, comment: String): Result<Boolean>
}