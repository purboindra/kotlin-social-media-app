package com.example.socialmedia.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SharedFileViewModel @Inject constructor() : ViewModel() {
    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri
    
    fun setVideouri(uri: Uri) {
        _videoUri.value = uri
    }
}