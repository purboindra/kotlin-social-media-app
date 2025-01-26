package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.PostModel

interface PostDatasource {
    suspend fun createPost(
        imageKey: String,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean>
    suspend fun fetchAllPosts(): Result<List<PostModel>>
}