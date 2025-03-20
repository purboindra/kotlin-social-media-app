package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import com.example.socialmedia.data.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
    ): Flow<State<Boolean>>
    
    fun subscribeToMessages(
        chatId: String,
        coroutineScope: CoroutineScope,
    ): Flow<List<SendMessageModel>>
    
    fun stopSubscription()
    
    suspend fun fetchMessages(): Flow<State<List<SendMessageModel>>>
}