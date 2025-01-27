package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostModel(
    @SerialName("id")
    val id: String,
    @SerialName("image_key")
    val imageKey: String,
    @SerialName("image_path")
    val imagePath: String,
    @SerialName("caption")
    val caption: String,
    @SerialName("tagged_users")
    val taggedUsers: List<String>?,
    @SerialName("tagged_location")
    val taggedLocation: String,
    @SerialName("user")
    val userId: String
)