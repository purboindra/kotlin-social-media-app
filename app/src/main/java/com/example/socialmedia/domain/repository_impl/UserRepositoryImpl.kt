package com.example.socialmedia.domain.repository_impl

import UserDatasource
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userDatasource: UserDatasource,
) : UserRepository {
    override suspend fun fetchAllUsers(query: String?): Flow<State<List<UserModel>>> =
        flow {
            emit(State.Loading)
            when (val result = userDatasource.fetchAllUsers(query)) {
                is ResponseModel.Success -> emit(State.Success(result.value))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun fetchUserById(userId: String): Flow<State<UserModel>> =
        flow {
            emit(State.Loading)
            when (val result = userDatasource.fetchUserById(userId)) {
                is ResponseModel.Success -> emit(State.Success(result.value))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun followUser(userId: String): Flow<State<Boolean>> =
        flow {
            emit(State.Loading)
            when (val result = userDatasource.followUser(userId)) {
                is ResponseModel.Success -> emit(State.Success(true))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun unFollowUser(userId: String): Flow<State<Boolean>> =
        flow {
            emit(State.Loading)
            when (val result = userDatasource.unFollowUser(userId)) {
                is ResponseModel.Success -> emit(State.Success(true))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
}