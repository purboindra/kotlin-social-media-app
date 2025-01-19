package com.example.socialmedia.data.datasource

import android.content.Context
import com.example.socialmedia.data.model.UserModel

interface AuthDatasource {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Boolean
    
    suspend fun login(email: String, password: String): Boolean
    
    suspend fun loginWithGoogle(context: Context): Result<Boolean>
    
    suspend fun checkUserExist(email: String): Boolean
}