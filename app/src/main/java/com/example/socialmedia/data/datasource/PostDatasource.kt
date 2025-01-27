package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.UploadImageModel

interface PostDatasource {
    suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean>
    
    suspend fun fetchAllPosts(): Result<List<PostModel>>
}