package com.example.socialmedia.data.datasource

interface PostDatasource {
    suspend fun createPost(
        imageKey: String,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean>
}