package com.example.socialmedia.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.socialmedia.data.model.UserModel
import java.util.Base64
import java.util.Date

object TokenConfig {
    const val SECRET_KEY =
        "Y7/pot+ImR+AIIdwkHeVvELFGxKNZXzXxtQjR1oXz0Gmn37SvesduB9JXz+EZZC85uO9+E+pUNdGBxWdrrt56g=="
    const val ISSUER = "https://nhgfvzozbtsrdmzpcobe.supabase.co/auth/v1"
    const val ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L
    const val REFRESH_TOKEN_EXPIRATION = 7 * 15 * 60 * 1000L
}

object TokenManager {
    private val algorithm = Algorithm.HMAC256(TokenConfig.SECRET_KEY)
    
    private val verifier: JWTVerifier =
        JWT.require(Algorithm.HMAC256(TokenConfig.SECRET_KEY))
            .withIssuer(TokenConfig.ISSUER)
            .build()
    
    fun createAccessToken(user: String): String {
        val jwt =
            JWT.create().withSubject(user).withIssuer(TokenConfig.ISSUER)
                .withIssuedAt(Date())
                .withExpiresAt(
                    Date(System.currentTimeMillis() + TokenConfig.ACCESS_TOKEN_EXPIRATION)
                ).sign(algorithm)
        
        return jwt
    }
    
    fun createRefreshToken(user: String): String {
        val jwt =
            JWT.create().withSubject(user).withIssuer(TokenConfig.ISSUER)
                .withIssuedAt(Date())
                .withExpiresAt(
                    Date(System.currentTimeMillis() + TokenConfig.REFRESH_TOKEN_EXPIRATION)
                ).sign(algorithm)
        
        return jwt
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            verifier.verify(token)
            true
        } catch (e: JWTVerificationException) {
            false
        }
    }
    
    fun getUserFromToken(token: String): UserModel? {
        return try {
            val decodedJWT = verifier.verify(token)
            
            val user = decodedJWT.getClaim("user_metadata").asMap()
            
            val userModel = UserModel(
                id = decodedJWT.getClaim("sub").asString(),
                username = user["name"] as String,
                fullName = user["full_name"] as String,
                email = user["email"] as String,
                profilePicture = user["avatar_url"] as String
            )
            
            userModel
        } catch (e: JWTVerificationException) {
            null
        }
    }
    
    fun getUSerIdFromToken(token: String): String? {
        return try {
            val decodedJWT = verifier.verify(token)
            
            val debugToken = mapOf(
                "payload" to decodedJWT.payload,
                "claims" to decodedJWT.claims
            )
            
            Log.d(
                "TokenManager",
                "debugToken data: ${
                    decodedJWT.getClaim("user_metadata").asMap()
                }"
            )
            
            Log.d("TokenManager", "debugToken: ${debugToken}")
            return decodedJWT.subject
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
            null
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun decodeJwtWithoutVerification(token: String): String {
        val parts = token.split(".")
        return String(Base64.getUrlDecoder().decode(parts[1]))
    }
    
    
}