package com.example.socialmedia.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InstastoryViewModel @Inject constructor() : ViewModel() {
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    
    private val _images = MutableStateFlow<List<Uri?>>(emptyList())
    val images = _images.asStateFlow()
    
    fun selectImage(uri: Uri) {
        _image.value = uri
    }
    
    fun addImages(images: List<Uri>) {
        _images.value = images
    }
}