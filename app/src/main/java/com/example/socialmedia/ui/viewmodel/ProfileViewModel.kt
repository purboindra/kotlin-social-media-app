package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.socialmedia.data.db.local.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStore: AppDataStore
) : ViewModel() {
    private val _profilePicture = MutableStateFlow<String?>(null)
    val profilePicture = _profilePicture.asStateFlow()
    
    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    
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
    
}