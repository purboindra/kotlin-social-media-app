package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreateInstaStoryModel(
    @SerialName("id")
    val id: String,
    @SerialName("user")
    val user: String,
    @SerialName("video_path")
    val videoPath: String,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("status")
    val status: Boolean,
    @SerialName("duration")
    val duration: Int
)