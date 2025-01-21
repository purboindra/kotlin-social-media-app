package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.socialmedia.data.db.local.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: AppDataStore
) : ViewModel() {
    
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    
    
}