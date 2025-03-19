package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.socialmedia.domain.usecases.MessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageUseCase: MessageUseCase
) : ViewModel() {



}