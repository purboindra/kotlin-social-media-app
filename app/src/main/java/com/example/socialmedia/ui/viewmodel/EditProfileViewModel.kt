package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUsecase: UserUsecase
) : ViewModel() {
    
    private val _userState = MutableStateFlow<State<UserModel>>(State.Idle)
    val userState = _userState.asStateFlow()
    
    private val _userNameState = MutableStateFlow<String>("")
    val userNameState = _userNameState
    
    private val _bioState = MutableStateFlow<String>("")
    val bioState = _bioState
    
    fun onUserNameChange(userName: String) {
        _userNameState.value = userName
    }
    
    fun onBioChange(bio: String) {
        _bioState.value = bio
    }
    
    fun fetchUserById(userId: String) = viewModelScope.launch {
        userUsecase.fetchUserById(userId).collectLatest { state ->
            _userState.value = state
            if (state is State.Success) {
                _userNameState.value = state.data.username ?: ""
                _bioState.value = state.data.bio ?: ""
            }
        }
    }
    
    
}