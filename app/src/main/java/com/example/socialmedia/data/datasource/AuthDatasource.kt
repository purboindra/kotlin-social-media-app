package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.UserModel

interface AuthDatasource {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Boolean
    
    suspend fun login(email: String, password: String): Boolean
}