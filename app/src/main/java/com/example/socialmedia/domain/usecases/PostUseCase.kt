package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.datasource_impl.FetchLikesModel
import com.example.socialmedia.data.datasource_impl.SavedPostModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.SavePostResult
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
    
    suspend fun fetchPostsById(userId: String): Flow<State<List<PostModel>>> {
        return postRepository.fetchPostsById(userId)
    }
    
    suspend fun createLike(id: String): Flow<State<Boolean>> {
        return postRepository.createLike(id)
    }
    
    suspend fun deleteLike(id: String): Flow<State<Boolean>> {
        return postRepository.deleteLike(id)
    }
    
    suspend fun fetchAllLikes(): Flow<State<List<FetchLikesModel>>> {
        return postRepository.fetchAllLikes()
    }
    
    suspend fun createComment(
        id: String,
        comment: String
    ): Flow<State<Boolean>> {
        return postRepository.createComment(id, comment)
    }
    
    suspend fun fetchSavedPostsByUserId(userId: String): Flow<State<List<SavedPostModel>>> {
        return postRepository.fetchSavedPostsByUserId(userId)
    }
    
    suspend fun savedPost(id: String): Flow<State<SavePostResult>> {
        return postRepository.savedPost(id)
    }
    
    suspend fun deleteSavedPost(id: String): Flow<State<SavePostResult>> {
        return postRepository.deleteSavedPost(id)
    }
}