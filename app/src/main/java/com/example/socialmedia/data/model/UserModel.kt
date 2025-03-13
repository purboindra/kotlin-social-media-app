package com.example.socialmedia.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: Instant? = null,
    @SerialName("access_token")
    val accessToken: String? = null,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("profile_picture")
    val profilePicture: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("birth_date")
    val birthDate: Instant? = null,
    @SerialName("providers")
    val providers: List<String>? = null,
    @SerialName("following")
    val following: List<String>? = null,
    @SerialName("followers")
    val followers: List<String>? = null,
    @SerialName("posts")
    val posts: List<String>? = null,
    @SerialName("bio")
    val bio: String? = null,
    val isFollow: Boolean = false
)

