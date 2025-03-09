package com.example.socialmedia.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    
    val logoutState by authViewModel.logoutState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(logoutState) {
        if (logoutState is State.Success) {
            navHostController.navigate(Screens.Login.route)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppElevatedButton(
            onClick = {
                coroutineScope.launch {
                    authViewModel.logout()
                }
            },
            text = "Logout"
        )
    }
}