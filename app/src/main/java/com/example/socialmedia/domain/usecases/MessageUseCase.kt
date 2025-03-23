package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.MessageModel
import com.example.socialmedia.data.model.SendMessageModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class MessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
    ): Flow<State<Boolean>> {
        return messageRepository.sendMessage(senderId, receiverId, message)
    }
    
    fun subscribeToMessages(
        chatId: String,
        coroutineScope: CoroutineScope,
    ): Flow<List<SendMessageModel>> {
        return messageRepository.subscribeToMessages(chatId, coroutineScope)
    }
    
    fun stopSubscription() {
        messageRepository.stopSubscription()
    }
    
    suspend fun fetchMessages(): Flow<State<List<MessageModel>>> {
        return messageRepository.fetchMessages()
    }
    
     suspend fun unSubscribeToMessage() {
        messageRepository.unSubscribeToMessage()
    }
    
    suspend fun fetchConversation(
        receiverId: String,
        senderId: String
    ): Flow<State<List<MessageModel>>> {
        return messageRepository.fetchConversation(receiverId, senderId)
    }
}