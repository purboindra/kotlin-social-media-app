package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.datasource_impl.SavedPostModel
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.usecases.PostUseCase
import com.example.socialmedia.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUsecase,
    private val postUseCase: PostUseCase,
    private val dataStore: AppDataStore
) : ViewModel() {
    
    private val _userState = MutableStateFlow<State<UserModel>>(State.Idle)
    val userState = _userState.asStateFlow()
    
    private val _profilePicture = MutableStateFlow<String?>(null)
    val profilePicture = _profilePicture.asStateFlow()
    
    private val _postsState =
        MutableStateFlow<State<List<PostModel>>>(State.Idle)
    val postsState = _postsState.asStateFlow()
    
    private val _savedPostsState =
        MutableStateFlow<State<List<SavedPostModel>>>(State.Idle)
    val savedPostsState = _savedPostsState.asStateFlow()
    
    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    
    private val _followState = MutableStateFlow<State<Boolean>>(State.Idle)
    val followState = _followState.asStateFlow()
    
    fun getProfilePicture() = viewModelScope.launch {
        dataStore.profilePicture.collectLatest { state ->
            _profilePicture.value = state
        }
    }
    
    fun getUserName() = viewModelScope.launch {
        dataStore.username.collectLatest { state ->
            _username.value = state
        }
    }
    
    val userId: StateFlow<String?> = dataStore.userId.stateIn(
        viewModelScope, SharingStarted.Lazily, ""
    )
    
    fun fetchPostsById(userId: String) = viewModelScope.launch {
        postUseCase.fetchPostsById(userId).collectLatest { state ->
            _postsState.value = state
        }
    }
    
    fun fetchUserById(userId: String) = viewModelScope.launch {
        userUseCase.fetchUserById(userId).collectLatest { state ->
            _userState.value = state
        }
    }
    
    fun fetchSavedPostsByUserId(userId: String) = viewModelScope.launch {
        postUseCase.fetchSavedPostsByUserId(userId).collectLatest { state ->
            _savedPostsState.value = state
        }
    }
    
    fun invokeFollow(userId: String) = viewModelScope.launch {
        _followState.value = State.Loading
        
        val currentState = _userState.value
        
        val currentUserId = dataStore.userId.firstOrNull() ?: ""
        
        if (currentState is State.Success) {
            val isCurrentlyFollowing = currentState.data.isFollow
            
            val followFlow = if (isCurrentlyFollowing) {
                userUseCase.unFollowUser(userId)
            } else {
                userUseCase.followUser(userId)
            }
            
            followFlow.collectLatest { state ->
                _followState.value = state
                
                if (state is State.Success) {
                    
                    val followers = currentState.data.followers?.toMutableList()
                        ?: mutableListOf()
                    val following = currentState.data.following?.toMutableList()
                        ?: mutableListOf()
                    
                    if (!isCurrentlyFollowing) {
                        followers.add(currentUserId)
                    } else {
                        followers.remove(currentUserId)
                    }
                    
                    _userState.value = currentState.copy(
                        data = currentState.data.copy(
                            isFollow = !isCurrentlyFollowing,
                            followers = followers,
                            following = following,
                        )
                    )
                }
            }
        }
    }
}