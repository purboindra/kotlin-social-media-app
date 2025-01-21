package com.example.socialmedia.domain.repository

import android.content.Context
import com.example.socialmedia.data.model.State
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Flow<State<Boolean>>
    
    suspend fun login(email: String, password: String): Flow<State<Boolean>>
    
    suspend fun loginWithGoogle(context: Context): Flow<State<Boolean>>
}