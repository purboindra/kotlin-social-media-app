package com.example.socialmedia.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.datasource_impl.FetchLikesModel
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CommentModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.SavePostResult
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.usecases.FileUseCase
import com.example.socialmedia.domain.usecases.PostUseCase
import com.example.socialmedia.utils.FileHelper
import com.example.socialmedia.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val fileUseCase: FileUseCase,
    private val postUseCase: PostUseCase,
    private val appDataStore: AppDataStore,
) : ViewModel() {
    
    private val _createPostState = MutableStateFlow<State<Boolean>>(State.Idle)
    val createPostState = _createPostState.asStateFlow()
    
    private val _postsState =
        MutableStateFlow<State<List<PostModel>>>(State.Idle)
    val postState = _postsState.asStateFlow()
    
    private val _likeState = MutableStateFlow<State<Boolean>>(State.Idle)
    val likeState = _likeState.asStateFlow()
    
    private val _allLikesState =
        MutableStateFlow<State<List<FetchLikesModel>>>(State.Idle)
    val allLikesState = _allLikesState.asStateFlow()
    
    private val _savedPostState =
        MutableStateFlow<State<SavePostResult>>(State.Idle)
    val savedPostState = _savedPostState.asStateFlow()
    
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    
    private val _images = MutableStateFlow<List<Uri>>(emptyList())
    val images = _images.asStateFlow()
    
    private val _caption = MutableStateFlow("")
    val caption = _caption.asStateFlow()
    
    fun selectImage(uri: Uri) {
        _image.value = uri
    }
    
    fun onChangeCaption(caption: String) {
        _caption.value = caption
    }
    
    fun addImage(images: List<Uri>) {
        _images.value = images
    }
    
    fun invokeSavedPost(id: String) = viewModelScope.launch {
        val currentState = _postsState.value
        
        if (currentState is State.Success) {
            val updatedPosts = currentState.data.map { post ->
                if (post.id == id) {
                    post.copy(
                        hasSaved = !(post.hasSaved)
                    )
                } else {
                    post
                }
            }
            
            _postsState.value = State.Success(updatedPosts)
            
            updatedPosts.firstOrNull()?.let {
                if (it.hasSaved) {
                    val result = postUseCase.savedPost(id)
                    
                    result.collectLatest { state ->
                        _savedPostState.value = state
                        
                        if (state is State.Failure) {
                            _postsState.value = State.Success(
                                currentState.data.map { post ->
                                    if (post.id == id) {
                                        post.copy(hasSaved = !post.hasSaved)
                                    } else {
                                        post
                                    }
                                    
                                }
                            )
                        }
                    }
                } else {
                    val result = postUseCase.deleteSavedPost(id)
                    
                    result.collectLatest { state ->
                        _savedPostState.value = state
                        
                        if (state is State.Failure) {
                            _postsState.value = State.Success(
                                currentState.data.map { post ->
                                    if (post.id == id) {
                                        post.copy(hasSaved = !post.hasSaved)
                                    } else {
                                        post
                                    }
                                    
                                }
                            )
                        }
                    }
                }
            }
            
            
        }
    }
    
    fun sendComment(id: String, commentInput: String) = viewModelScope.launch {
        val currentState = _postsState.value
        
        val token = appDataStore.accessToken.firstOrNull() ?: "No Token"
        val user = TokenManager.getUserFromToken(token)
        
        if (currentState is State.Success) {
            val updatedPosts = currentState.data.map { post ->
                if (post.id == id) {
                    if (user == null) return@map post
                    
                    val comment = CommentModel(
                        id = "",
                        comment = commentInput,
                        postId = id,
                        createdAt = "",
                        user = user,
                    )
                    post.copy(
                        comments = post.comments?.plus(
                            comment
                        )
                    )
                } else {
                    post
                }
            }
            _postsState.value = State.Success(updatedPosts)
        }
        
        postUseCase.createComment(
            id,
            comment = commentInput
        ).collectLatest { state ->
            Log.d("Comment", "State: $state")
        }
        
    }
    
    fun invokeLike(id: String) = viewModelScope.launch {
        val currentState = _postsState.value
        if (currentState is State.Success) {
            val updatedPosts = currentState.data.map { post ->
                if (post.id == id) {
                    post.copy(hasLike = !(post.hasLike ?: false))
                } else post
            }
            
            _postsState.value = State.Success(updatedPosts)
            
            updatedPosts.firstOrNull { it.id == id }?.let { post ->
                launch {
                    if (post.hasLike == true) {
                        postUseCase.createLike(id).collectLatest { state ->
                            _likeState.value = state
                        }
                    } else {
                        postUseCase.deleteLike(id).collectLatest { state ->
                            _likeState.value = state
                        }
                    }
                }
            }
        }
    }
    
    fun fetchAllLikes() = viewModelScope.launch {
        _allLikesState.emit(State.Loading)
        postUseCase.fetchAllLikes().collectLatest { state ->
            _allLikesState.value = state
        }
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
        
        fileUseCase.uploadFile(imageBytes, "posts").collectLatest { state ->
            handleFileUploadState(state)
        }
    }
    
    private suspend fun handleFileUploadState(state: State<UploadImageModel?>) {
        when (state) {
            is State.Success -> {
                val image = state.data
                image?.let {
                    val uploadImageModel = UploadImageModel(
                        id = it.id,
                        key = it.key,
                        path = it.path,
                    )
                    
                    postUseCase.createPost(
                        image = uploadImageModel,
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