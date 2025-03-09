package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstaStoryModel(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("user")
    val user: UserModel,
    @SerialName("video_path")
    val videoPath: String,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("status")
    val status: Boolean,
    @SerialName("duration")
    val duration: Int
)