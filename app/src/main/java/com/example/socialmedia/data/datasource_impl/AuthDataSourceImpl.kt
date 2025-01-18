package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.AuthDatasource
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.utils.PasswordUtils
import com.example.socialmedia.utils.TokenManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

private val TAG = "AuthDataSourceImpl"

class AuthDataSourceImpl(
    private val supabase: SupabaseClient
) : AuthDatasource {
    override suspend fun register(email: String, password: String, username: String): Boolean {
        
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            val currentUser = supabase.auth.currentUserOrNull()
            
            val userInfo = currentUser ?: throw Exception("Something went wrong. Please try again!")
            
            Log.d(TAG, "Attempting to sign up with email: $email and password: $password")
            
            Log.d(TAG, "User sign up with email: $userInfo")
            
            val createdAt = userInfo.createdAt
            
            val userId = userInfo.id
            
            val hashPassword = PasswordUtils.hashPassword(password)
            
            val accessToken = TokenManager.createAccessToken(userId)
            val refreshToken = TokenManager.createRefreshToken(userId)
            
            val userModel = UserModel(
                id = userId,
                email = email,
                username = username,
                profilePicture = "",
                password = hashPassword,
                createdAt = createdAt!!,
                accessToken = accessToken,
                refreshToken = refreshToken,
                fullName = username,
            )
            
            supabase.from("users").insert(userModel)
            
            return true
            
        } catch (e: Exception) {
            Log.e(TAG, "Error registering: ${e.message}")
            throw e
        }
    }
}