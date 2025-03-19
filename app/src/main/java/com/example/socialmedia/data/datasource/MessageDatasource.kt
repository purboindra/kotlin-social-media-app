package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface MessageDatasource {
    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
    ): ResponseModel<Boolean>
    
    suspend fun subscribeToMessage(
        chatId: String, coroutineScope: CoroutineScope,
        onMessageReceived: (SendMessageModel) -> Unit
    ): Job
}