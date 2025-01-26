package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepositoryImpl(
    private val postDataSource: PostDatasource
) : PostRepository {
    override suspend fun createPost(
        imageKey: String,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String
    ): Flow<State<Boolean>> = flow {
        try {
            val result = postDataSource.createPost(
                imageKey,
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
}