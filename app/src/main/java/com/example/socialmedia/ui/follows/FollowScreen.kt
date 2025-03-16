package com.example.socialmedia.ui.follows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.viewmodel.FollowsViewModel
import com.example.socialmedia.utils.ConnectionType

@Composable
fun FollowScreen(
    userId: String,
    type: ConnectionType,
    navHostController: NavHostController,
    followsViewModel: FollowsViewModel = hiltViewModel()
) {
    
    
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
        
        }
    }
}