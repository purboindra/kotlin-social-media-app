package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.MessageDatasource
import com.example.socialmedia.data.model.MessageModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class MessageRepositoryImpl(
    private val messageDatasource: MessageDatasource
) : MessageRepository {
    
    
    override suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String
    ): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        val result =
            messageDatasource.sendMessage(senderId, receiverId, message)
        when (result) {
            is ResponseModel.Success -> emit(State.Success(result.value))
            is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
        }
    }
    
    override fun subscribeToMessages(
        chatId: String,
        coroutineScope: CoroutineScope
    ): Flow<List<SendMessageModel>> = channelFlow {
        messageDatasource.subscribeToMessage(
            chatId,
            coroutineScope
        ) { newMessage ->
            trySend(listOf(newMessage))
        }
    }
    
    override fun stopSubscription() {
    }
    
    override suspend fun fetchMessages(): Flow<State<List<MessageModel>>> =
        flow {
            when (val result = messageDatasource.fetchMessages()) {
                is ResponseModel.Success -> emit(State.Success(result.value))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun fetchConversation(
        receiverId: String,
        senderId: String
    ): Flow<State<List<MessageModel>>> = flow {
        when (val result =
            messageDatasource.fetchConversation(receiverId, senderId)) {
            is ResponseModel.Success -> emit(State.Success(result.value))
            is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
        }
    }
    
    override suspend fun unSubscribeToMessage() {
        messageDatasource.unSubscribeToMessage()
    }
}