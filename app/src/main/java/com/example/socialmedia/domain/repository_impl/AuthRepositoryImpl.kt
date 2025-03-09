package com.example.socialmedia.domain.repository_impl

import android.content.Context
import com.example.socialmedia.data.datasource.AuthDatasource
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val authDatasource: AuthDatasource) :
    AuthRepository {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        try {
            val response = authDatasource.register(email, password, username)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
    
    override suspend fun login(
        email: String,
        password: String
    ): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        try {
            val response = authDatasource.login(email, password)
            emit(State.Success(response.isSuccess))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
    
    override suspend fun loginWithGoogle(context: Context): Flow<State<Boolean>> =
        flow {
            emit(State.Loading)
            try {
                val response = authDatasource.loginWithGoogle(context)
                response.onSuccess {
                    emit(State.Success(true))
                }.onFailure {
                    emit(State.Failure(it))
                }
            } catch (e: Exception) {
                emit(State.Failure(e))
            }
        }
    
    override suspend fun logout(): Flow<State<ResponseModel<Boolean>>> = flow {
        emit(State.Loading)
        try {
            authDatasource.logout()
            emit(State.Success(ResponseModel.Success(true)))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
        
    }
}