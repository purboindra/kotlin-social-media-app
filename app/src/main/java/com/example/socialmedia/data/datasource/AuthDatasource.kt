package com.example.socialmedia.data.datasource

import android.content.Context

interface AuthDatasource {
    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Boolean
    
    suspend fun login(email: String, password: String): Result<Boolean>
    
    suspend fun loginWithGoogle(context: Context): Result<Boolean>
    
    suspend fun checkUserExist(email: String): Result<Boolean>
}