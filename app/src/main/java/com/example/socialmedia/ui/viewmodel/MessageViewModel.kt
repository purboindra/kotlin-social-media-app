package com.example.socialmedia.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.MessageModel
import com.example.socialmedia.data.model.SendMessageModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.MessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageUseCase: MessageUseCase,
    private val appDataStore: AppDataStore,
) : ViewModel() {
    
    private val _messagesState =
        MutableStateFlow<State<List<MessageModel>>>(State.Idle)
    val messagesState = _messagesState.asStateFlow()
    
    private val _sendMessageState = MutableStateFlow<State<Boolean>>(State.Idle)
    val sendMessageState = _sendMessageState.asStateFlow()
    
    private val _conversationState =
        MutableStateFlow<State<List<MessageModel>>>(State.Idle)
    val conversationState = _conversationState.asStateFlow()
    
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()
    
    fun onMessageChange(message: String) {
        _message.value = message
    }
    
    val userId: StateFlow<String?> = appDataStore.userId.stateIn(
        viewModelScope, SharingStarted.Lazily, null
    )
    
    fun unSubscribeToMessage() {
        viewModelScope.launch {
            messageUseCase.unSubscribeToMessage()
        }
    }
    
    fun fetchConversation(receiverId: String) {
        viewModelScope.launch {
            messageUseCase.fetchConversation(
                receiverId = receiverId,
                senderId = userId.value ?: ""
            ).collectLatest { state ->
                _conversationState.value = state
            }
        }
    }
    
    fun sendMessage(receiverId: String) {
        viewModelScope.launch {
            messageUseCase.sendMessage(
                senderId = userId.value ?: "",
                receiverId = receiverId,
                message = _message.value,
            ).collectLatest { state ->
                _sendMessageState.value = state
            }
        }
    }
    
    fun subscribeToMessages(chatId: String) {
        viewModelScope.launch {
            messageUseCase.subscribeToMessages(chatId, viewModelScope)
                .collectLatest { state ->
                    Log.d("MessageViewModel", "subscribeToMessages: $state")
                }
        }
    }
    
    fun fetchMessages() {
        viewModelScope.launch {
            messageUseCase.fetchMessages().collectLatest { state ->
                _messagesState.value = state
            }
        }
    }
}