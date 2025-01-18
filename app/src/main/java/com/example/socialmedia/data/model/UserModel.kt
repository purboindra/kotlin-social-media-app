package com.example.socialmedia.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("username")
    val username: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("password")
    val password: String,
    @SerialName("profile_picture")
    val profilePicture: String,
    @SerialName("email")
    val email: String,
    @SerialName("birth_date")
    val birthDate: Instant? = null
    // TODO POSTS
)

