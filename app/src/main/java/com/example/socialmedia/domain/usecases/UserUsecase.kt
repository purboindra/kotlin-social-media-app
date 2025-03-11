package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserUsecase(
    private val userRepository: UserRepository
) {
    suspend fun search(query: String): Flow<State<List<UserModel>>> {
        return userRepository.search(query)
    }
}