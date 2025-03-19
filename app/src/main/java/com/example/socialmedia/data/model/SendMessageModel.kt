package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageModel(
    @SerialName("message")
    val message: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    @SerialName("status")
    val status: String = "",
    @SerialName("media_url")
    val mediaUrl: String = "",
    @SerialName("deleted")
    val deleted: Boolean = false
)