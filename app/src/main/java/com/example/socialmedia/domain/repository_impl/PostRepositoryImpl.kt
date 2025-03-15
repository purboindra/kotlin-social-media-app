package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.PostDatasource
import com.example.socialmedia.data.datasource_impl.FetchLikesModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SavePostResult
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
                image, caption, taggedUsers, taggedLocation
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
        emit(State.Loading)
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
    
    override suspend fun fetchPostsById(userId: String): Flow<State<List<PostModel>>> =
        flow {
            emit(State.Loading)
            when (val result = postDataSource.fetchPostsById(userId)) {
                is ResponseModel.Success -> emit(State.Success(result.value))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
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
    
    override suspend fun fetchAllLikes(): Flow<State<List<FetchLikesModel>>> =
        flow {
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
    
    override suspend fun createComment(
        id: String, comment: String
    ): Flow<State<Boolean>> = flow {
        try {
            
            val result = postDataSource.createComment(id, comment)
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun savedPost(id: String): Flow<State<SavePostResult>> =
        flow {
            when (val result = postDataSource.savedPost(id)) {
                is SavePostResult.Success -> emit(State.Success(result))
                is SavePostResult.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun deleteSavedPost(id: String): Flow<State<SavePostResult>> =
        flow {
            when (val result = postDataSource.deleteSavedPost(id)) {
                is SavePostResult.Success -> emit(State.Success(result))
                is SavePostResult.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
}