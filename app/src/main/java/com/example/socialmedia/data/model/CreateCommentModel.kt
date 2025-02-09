package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentModel(
    @SerialName("id")
    val id: String? = null,
    @SerialName("comment")
    val comment: String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("user_id")
    val userId: String
)