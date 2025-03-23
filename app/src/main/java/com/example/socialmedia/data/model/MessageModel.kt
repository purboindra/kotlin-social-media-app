package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("message")
    val message: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("status")
    val status: String? = null,
    @SerialName("media_url")
    val mediaUrl: String? = null,
    @SerialName("deleted")
    val deleted: Boolean? = null
)