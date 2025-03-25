package com.example.socialmedia.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.usecases.FileUseCase
import com.example.socialmedia.domain.usecases.InstaStoryUseCase
import com.example.socialmedia.utils.FileHelper
import com.example.socialmedia.utils.PostHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstastoryViewModel @Inject constructor(
    private val instaStoryUseCase: InstaStoryUseCase,
    private val fileUseCase: FileUseCase,
    private val appDataStore: AppDataStore,
) : ViewModel() {
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    
    private val _images = MutableStateFlow<List<Uri?>>(emptyList())
    val images = _images.asStateFlow()
    
    private val _instastoriesState =
        MutableStateFlow<State<List<InstaStory>>>(State.Idle)
    val instastoriesState = _instastoriesState.asStateFlow()
    
    private val _postInstaStoryState =
        MutableStateFlow<State<ResponseModel<Boolean>>>(State.Idle)
    val postInstaStoryState = _postInstaStoryState.asStateFlow()
    
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()
    
    val userId: StateFlow<String?> = appDataStore.userId.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    
    fun selectImage(uri: Uri?) {
        _image.value = uri
    }
    
    fun addImages(images: List<Uri>) {
        _images.value = images
    }
    
    fun fetchAllInstastories() = viewModelScope.launch {
        instaStoryUseCase.fetchAllInstastories().collectLatest { state ->
            _instastoriesState.value = state
        }
    }
    
    fun getUserId(context: Context) = viewModelScope.launch {
        val dataStore = AppDataStore(context)
        dataStore.userId.collectLatest { state ->
            _currentUserId.value = state
        }
    }
    
    fun uploadInstaStory(context: Context, imageUri: Uri) =
        viewModelScope.launch {
            _postInstaStoryState.emit(State.Loading)
            val imageBytes =
                FileHelper.uriToByteArray(context.contentResolver, imageUri)
            
            if (imageBytes == null) {
                _postInstaStoryState.value =
                    State.Failure(Exception("Failed to process image"))
                return@launch
            }
            
            fileUseCase.uploadFile(imageBytes, "instastories")
                .collectLatest { state ->
                    
                    when (state) {
                        is State.Success -> {
                            val image = state.data
                            image?.let {
                                val uploadImageModel = UploadImageModel(
                                    id = it.id,
                                    key = it.key,
                                    path = it.path
                                )
                                instaStoryUseCase.createInstaStory(
                                    uploadImageModel
                                )
                                    .collectLatest { instaStoryState ->
                                        _postInstaStoryState.value =
                                            instaStoryState
                                    }
                            }
                        }
                        
                        is State.Failure -> {
                            Log.e(
                                "Create Insta Story",
                                "Error uploading file: ${state.throwable.message}"
                            )
                            _postInstaStoryState.value =
                                State.Failure(state.throwable)
                        }
                        
                        else -> {}
                    }
                }
        }
}