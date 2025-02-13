package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSavePostModel(
    @SerialName("user_id")
    val userId: String,
    @SerialName("post_id")
    val postId: String,
)