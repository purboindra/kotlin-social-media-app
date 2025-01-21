package com.example.socialmedia.ui.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.socialmedia.data.model.SnackbarConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SnackbarViewModel @Inject constructor() : ViewModel() {
    
    private val _snackbarState = MutableStateFlow<SnackbarConfig?>(null)
    val snackbarState: StateFlow<SnackbarConfig?> = _snackbarState
    
    fun showSnackbar(message: String, isError: Boolean = false) {
        val config = SnackbarConfig(
            message = message,
            backgroundColor = if (isError) Color.Red else Color.Green,
            contentColor = Color.White,
            duration = SnackbarDuration.Long
        )
        _snackbarState.value = config
    }
}