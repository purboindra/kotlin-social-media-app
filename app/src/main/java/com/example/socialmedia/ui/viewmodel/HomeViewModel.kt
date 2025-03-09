package com.example.socialmedia.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.FileUseCase
import com.example.socialmedia.domain.usecases.InstaStoryUseCase
import com.example.socialmedia.utils.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val instaStoryUseCase: InstaStoryUseCase,
    private val fileUseCase: FileUseCase,
) : ViewModel() {
    private val _instaStoryState =
        MutableStateFlow<State<ResponseModel<Boolean>>>(State.Idle)
    val instaStoryState: StateFlow<State<ResponseModel<Boolean>>> = _instaStoryState
    
    @OptIn(ExperimentalCoroutinesApi::class)
    fun createInstaStory(video: Uri, context: Context) = viewModelScope.launch {
        _instaStoryState.value = State.Loading
        
        val videoBytes =
            FileHelper.uriToByteArray(context.contentResolver, video)
                ?: return@launch run {
                    _instaStoryState.value =
                        State.Failure(Throwable("Failed to convert video to bytes"))
                }
        
        fileUseCase.uploadFile(videoBytes,"instastories")
            .flatMapLatest { state ->
                when (state) {
                    is State.Success -> {
                        state.data?.let { uploadImageModel ->
                            instaStoryUseCase.createInstaStory(uploadImageModel)
                        }
                            ?: flowOf(State.Failure(Throwable("Failed to upload video")))
                    }
                    
                    is State.Failure -> flowOf(state)
                    else -> flowOf(State.Loading)
                }
            }
            .collectLatest { _instaStoryState.value = it }
        
    }
}
