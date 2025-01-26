package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.State
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun createPost(
        imageKey: String,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Flow<State<Boolean>>
}