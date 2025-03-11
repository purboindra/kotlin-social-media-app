package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun search(query: String): Flow<List<UserModel>>
}