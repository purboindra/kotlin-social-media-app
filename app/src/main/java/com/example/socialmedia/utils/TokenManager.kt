package com.example.socialmedia.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.Date

object TokenConfig {
    const val SECRET_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    const val ISSUER = "com.example.socialmedia"
    const val ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L
    const val REFRESH_TOKEN_EXPIRATION = 7 * 15 * 60 * 1000L
}

object TokenManager {
    private val algorithm = Algorithm.HMAC256(TokenConfig.SECRET_KEY)
    
    private val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(TokenConfig.SECRET_KEY))
        .withIssuer(TokenConfig.ISSUER)
        .build()
    
    fun createAccessToken(userId: String): String {
        val jwt =
            JWT.create().withSubject(userId).withIssuer(TokenConfig.ISSUER).withIssuedAt(Date())
                .withExpiresAt(
                    Date(System.currentTimeMillis() + TokenConfig.ACCESS_TOKEN_EXPIRATION)
                ).sign(algorithm)
        
        return jwt
    }
    
    fun createRefreshToken(userId: String): String {
        val jwt =
            JWT.create().withSubject(userId).withIssuer(TokenConfig.ISSUER).withIssuedAt(Date())
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
    
    fun getUSerIdFromToken(token: String): String? {
        return try {
            val decodedJWT = verifier.verify(token)
            return decodedJWT.subject
        } catch (e: JWTVerificationException) {
            null
        }
    }
    
}