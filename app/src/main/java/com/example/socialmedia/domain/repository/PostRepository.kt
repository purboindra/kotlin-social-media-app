package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Flow<State<Boolean>>
    
    suspend fun fetchAllPosts(): Flow<State<List<PostModel>>>
}