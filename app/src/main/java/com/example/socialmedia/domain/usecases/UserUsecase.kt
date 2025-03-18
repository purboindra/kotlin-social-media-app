package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.FollowsUserModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserUsecase(
    private val userRepository: UserRepository
) {
    suspend fun fetchAllUsers(query: String?): Flow<State<List<UserModel>>> {
        return userRepository.fetchAllUsers(query)
    }
    
    suspend fun fetchUserById(userId: String): Flow<State<UserModel>> {
        return userRepository.fetchUserById(userId)
    }
    
    suspend fun followUser(userId: String): Flow<State<Boolean>> {
        return userRepository.followUser(userId)
    }
    
    suspend fun unFollowUser(userId: String): Flow<State<Boolean>> {
        return userRepository.unFollowUser(userId)
    }
    
    suspend fun fetchUserFollowing(
        userId: String,
        query: String?
    ): Flow<State<List<FollowsUserModel>>> {
        return userRepository.fetchUserFollowing(userId, query)
    }
    
    suspend fun fetchUserFollowers(
        userId: String,
        query: String?
    ): Flow<State<List<FollowsUserModel>>> {
        return userRepository.fetchUserFollowers(userId, query)
    }
}