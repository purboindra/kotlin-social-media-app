package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.viewmodel.ProfileViewModel

@Composable
fun ProfilePostGridCompose(
    profileViewModel: ProfileViewModel
) {
    val postsState by profileViewModel.postsState.collectAsState()
    
    when (postsState) {
        is State.Success -> {
            val data = (postsState as State.Success).data
            if (data.isEmpty()) Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Post Yet")
            } else
                PostGridCompose(data)
        }
        
        is State.Failure -> {
            val message =
                (postsState as State.Failure).throwable.message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(message ?: "Unknown Error Occurred")
            }
        }
        
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}