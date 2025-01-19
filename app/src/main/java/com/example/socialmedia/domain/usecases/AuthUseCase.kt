package com.example.socialmedia.domain.usecases

import android.content.Context
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val authRepository: AuthRepository) {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Flow<State<Boolean>> {
        return authRepository.register(email, password, username)
    }
    
    suspend fun login(email: String, password: String): Flow<State<Boolean>> {
        return authRepository.login(email, password)
    }
    
    suspend fun loginWithGoogle(context: Context): Flow<State<Boolean>> {
        return authRepository.loginWithGoogle(context)
    }
}
