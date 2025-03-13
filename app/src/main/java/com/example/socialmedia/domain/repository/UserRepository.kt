package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchAllUsers(query: String?): Flow<State<List<UserModel>>>
    suspend fun fetchUserById(userId: String): Flow<State<UserModel>>
    suspend fun followUser(userId: String): Flow<State<Boolean>>
    suspend fun unFollowUser(userId: String): Flow<State<Boolean>>
}