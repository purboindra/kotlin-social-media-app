package com.example.socialmedia.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostModel(
    @SerialName("id")
    val id:String,
    @SerialName("user")
    val userId:String,
    @SerialName("caption")
    val caption:String,
    @SerialName("image_key")
    val imageKey:String,
    @SerialName("tagged_users")
    val taggedUsers:List<String>,
    @SerialName("tagged_location")
    val taggedLocation:String,
    @SerialName("created_at")
    val createdAt:Instant,
    @SerialName("likes")
    val likes:List<String>
)