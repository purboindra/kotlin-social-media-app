package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import kotlinx.coroutines.flow.Flow

private val TAG = "AuthRepository"

interface AuthRepository {
    suspend fun register(email: String, password: String, username: String): Flow<State<Boolean>>
}