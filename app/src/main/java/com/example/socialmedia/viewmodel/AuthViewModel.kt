package com.example.socialmedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ObsecurePasswordType {
    Password, ConfirmPassword
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    
    private val _registerState: MutableStateFlow<State<Boolean>> = MutableStateFlow(State.Idle)
    val registerState = _registerState.asStateFlow()
    
    private val _hasObsecurePassword = MutableStateFlow(true)
    val hasObsecurePassword = _hasObsecurePassword.asStateFlow()
    
    private val _hasObsecureConfirmPassword = MutableStateFlow(true)
    val hasObsecureConfirmPassword = _hasObsecureConfirmPassword.asStateFlow()
    
    private val _passwordText = MutableStateFlow("")
    val passwordText = _passwordText.asStateFlow()
    
    private val _confirmPasswordText = MutableStateFlow("")
    val confirmPasswordText = _confirmPasswordText.asStateFlow()
    
    private val _usernameText = MutableStateFlow("")
    val usernameText = _usernameText.asStateFlow()
    
    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()
    
    fun onChangeUsernameText(username: String) {
        _usernameText.value = username
    }
    
    fun onChangeEmailText(email: String) {
        _emailText.value = email
    }
    
    fun onChangePasswordText(password: String) {
        _passwordText.value = password
    }
    
    fun onChangeConfirmPasswordText(confirmPassword: String) {
        _confirmPasswordText.value = confirmPassword
    }
    
    fun toggleObsecure(type: ObsecurePasswordType) {
        if (type == ObsecurePasswordType.Password) {
            _hasObsecurePassword.value = !_hasObsecurePassword.value
        } else {
            _hasObsecureConfirmPassword.value = !_hasObsecureConfirmPassword.value
        }
    }
    
    fun register() = viewModelScope.launch {
        authUseCase.register(
            email = _emailText.value,
            password = _passwordText.value,
            username = _usernameText.value,
        ).collectLatest { state ->
            _registerState.value = state
        }
    }
}