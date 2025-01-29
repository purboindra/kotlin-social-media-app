package com.example.socialmedia.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeModel(
    @SerialName("id")
    val id: String? = null,
    @SerialName("post_id")
    val postId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("created_at")
    val createdAt: String? = null,
)