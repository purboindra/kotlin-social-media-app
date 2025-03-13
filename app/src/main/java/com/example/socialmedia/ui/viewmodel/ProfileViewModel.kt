package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.usecases.UserUsecase
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
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUsecase,
    private val dataStore: AppDataStore
) : ViewModel() {
    
    private val _userState = MutableStateFlow<State<UserModel>>(State.Idle)
    val userState = _userState.asStateFlow()
    
    private val _profilePicture = MutableStateFlow<String?>(null)
    val profilePicture = _profilePicture.asStateFlow()
    
    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    
    private val _followState = MutableStateFlow(false)
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
    
    fun fetchUserById(userId: String) = viewModelScope.launch {
        userUseCase.fetchUserById(userId).collectLatest { state ->
            _userState.value = state
        }
    }
    
    fun invokeFollow(userId: String) = viewModelScope.launch {
    
    }
}