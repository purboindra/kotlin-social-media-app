package com.example.socialmedia.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _hasObsecurePassword = MutableStateFlow(true)
    val hasObsecurePassword = _hasObsecurePassword.asStateFlow()

    private val _passwordText = MutableStateFlow("")
    val passwordText = _passwordText.asStateFlow()

    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()

    fun onChangeEmailText(email: String) {
        _emailText.value = email
    }

    fun onChangePasswordText(password: String) {
        _passwordText.value = password
    }

    fun toggleObsecure() {
        _hasObsecurePassword.update {
            !it
        }
    }
}