package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.LikeModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class PostUseCase(private val postRepository: PostRepository) {
    suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String
    ): Flow<State<Boolean>> {
        return postRepository.createPost(
            image,
            caption,
            taggedUsers,
            taggedLocation
        )
    }
    
    suspend fun fetchAllPosts(): Flow<State<List<PostModel>>> {
        return postRepository.fetchAllPosts()
    }
    
    suspend fun createLike(id: String): Flow<State<Boolean>> {
        return postRepository.createLike(id)
    }
    
    suspend fun deleteLike(id: String): Flow<State<Boolean>> {
        return postRepository.deleteLike(id)
    }
    
    suspend fun fetchAllLikes(): Flow<State<List<LikeModel>>> {
        return postRepository.fetchAllLikes()
    }
}