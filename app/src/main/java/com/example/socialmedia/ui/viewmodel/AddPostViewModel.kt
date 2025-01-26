package com.example.socialmedia.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor() : ViewModel() {
    
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    
    fun selectImage(uri: Uri) {
        _image.value = uri
    }
    
}