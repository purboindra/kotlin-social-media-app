package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.model.SendMessageModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.MessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageUseCase: MessageUseCase
) : ViewModel() {
    
    private val _messagesState =
        MutableStateFlow<State<List<SendMessageModel>>>(State.Idle)
    val messagesState = _messagesState.asStateFlow()
    
    private val _sendMessageState = MutableStateFlow<State<Boolean>>(State.Idle)
    val sendMessageState = _sendMessageState.asStateFlow()
    
    fun fetchMessages() {
        viewModelScope.launch {
            messageUseCase.fetchMessages().collectLatest { state ->
                _messagesState.value = state
            }
        }
    }
}