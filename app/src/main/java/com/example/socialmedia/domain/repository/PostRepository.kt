package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.LikeModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.SavePostResult
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
    
    suspend fun createLike(id: String): Flow<State<Boolean>>
    
    suspend fun deleteLike(id: String): Flow<State<Boolean>>
    
    suspend fun fetchAllLikes(): Flow<State<List<LikeModel>>>
    
    suspend fun createComment(id: String, comment: String): Flow<State<Boolean>>
    
    suspend fun savedPost(id: String): Flow<State<SavePostResult>>
    suspend fun deleteSavedPost(id: String): Flow<State<SavePostResult>>
}