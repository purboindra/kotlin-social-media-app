package com.example.socialmedia.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostModel(
    @SerialName("id")
    val id: String,
    @SerialName("user")
    val user: UserModel,
    @SerialName("caption")
    val caption: String,
    @SerialName("image_key")
    val imageKey: String,
    @SerialName("image_path")
    val imagePath: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("tagged_users")
    val taggedUsers: List<String>? = null,
    @SerialName("tagged_location")
    val taggedLocation: String? = null,
    @SerialName("created_at")
    val createdAt: Instant,
    val hasLike:Boolean?=false,
    @SerialName("comments")
    val comments: List<CommentModel>? = null
)