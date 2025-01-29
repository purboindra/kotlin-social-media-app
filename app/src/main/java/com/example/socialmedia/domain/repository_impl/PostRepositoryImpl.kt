package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.model.LikeModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepositoryImpl(
    private val postDataSource: PostDatasource
) : PostRepository {
    override suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String
    ): Flow<State<Boolean>> = flow {
        try {
            val result = postDataSource.createPost(
                image,
                caption,
                taggedUsers,
                taggedLocation
            )
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun fetchAllPosts(): Flow<State<List<PostModel>>> = flow {
        try {
            val result = postDataSource.fetchAllPosts()
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun createLike(id: String): Flow<State<Boolean>> = flow {
        try {
            val result = postDataSource.createLike(id)
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun deleteLike(id: String): Flow<State<Boolean>> = flow {
        try {
            val result = postDataSource.deleteLike(id)
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun fetchAllLikes(): Flow<State<List<LikeModel>>> = flow {
        try {
            val result = postDataSource.fetchAllLikes()
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
}