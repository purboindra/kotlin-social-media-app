package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentModel(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("user")
    val user: UserModel,
    @SerialName("post_id")
    val postId: String,
    @SerialName("likes")
    val likes: List<String> = emptyList(),
)