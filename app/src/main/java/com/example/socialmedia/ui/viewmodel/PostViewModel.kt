package com.example.socialmedia.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.FileUseCase
import com.example.socialmedia.domain.usecases.PostUseCase
import com.example.socialmedia.utils.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val fileUseCase: FileUseCase,
    private val postUseCase: PostUseCase,
) : ViewModel() {
    
    private val _createPostState = MutableStateFlow<State<Boolean>>(State.Idle)
    val createPostState = _createPostState.asStateFlow()
    
    private val _postsState =
        MutableStateFlow<State<List<PostModel>>>(State.Idle)
    val postState = _postsState.asStateFlow()
    
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    
    private val _caption = MutableStateFlow("")
    val caption = _caption.asStateFlow()
    
    fun selectImage(uri: Uri) {
        _image.value = uri
    }
    
    fun onChangeCaption(caption: String) {
        _caption.value = caption
    }
    
    fun fetchAllPosts() = viewModelScope.launch {
        postUseCase.fetchAllPosts().collectLatest { state ->
            _postsState.value = state
        }
    }
    
    fun createPost(context: Context, imageUri: Uri) = viewModelScope.launch {
        
        _createPostState.emit(State.Loading)
        
        val imageBytes =
            FileHelper.uriToByteArray(context.contentResolver, imageUri)
        if (imageBytes == null) {
            _createPostState.value =
                State.Failure(Exception("Failed to process image"))
            return@launch
        }
        
        fileUseCase.uploadFile(imageBytes).collectLatest { state ->
            handleFileUploadState(state)
        }
    }
    
    private suspend fun handleFileUploadState(state: State<String?>) {
        when (state) {
            is State.Success -> {
                val imageKey = state.data
                if (!imageKey.isNullOrEmpty()) {
                    postUseCase.createPost(
                        imageKey = imageKey,
                        caption = _caption.value,
                        taggedUsers = null,
                        taggedLocation = ""
                    ).collectLatest { createPostState ->
                        _createPostState.value = createPostState
                    }
                }
            }
            
            is State.Failure -> {
                Log.e(
                    "CreatePost",
                    "Error uploading file: ${state.throwable.message}"
                )
                _createPostState.value = State.Failure(state.throwable)
            }
            
            else -> {}
        }
    }
}