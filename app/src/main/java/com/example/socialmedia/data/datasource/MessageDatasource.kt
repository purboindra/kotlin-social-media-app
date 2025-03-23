package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.MessageModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

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
    
    suspend fun unSubscribeToMessage()
    
    suspend fun fetchMessages(): ResponseModel<List<MessageModel>>
    
    suspend fun fetchConversation(
        receiverId: String,
        senderId: String
    ): ResponseModel<List<MessageModel>>
}