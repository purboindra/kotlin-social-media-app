package com.example.socialmedia.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.ProfileCaptionCompose
import com.example.socialmedia.ui.components.ProfileHeaderCompose
import com.example.socialmedia.ui.components.ProfileHeaderComposeParams
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.viewmodel.AuthViewModel
import com.example.socialmedia.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    
    val logoutState by authViewModel.logoutState.collectAsState()
    val profilePicture by profileViewModel.profilePicture.collectAsState()
    val userName by profileViewModel.username.collectAsState()
    
    LaunchedEffect(logoutState) {
        if (logoutState is State.Success) {
            navHostController.navigate(Screens.Login.route)
        }
    }
    
    LaunchedEffect(Unit) {
        profileViewModel.getProfilePicture()
        profileViewModel.getUserName()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(userName ?: "Profile")
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                ProfileHeaderCompose(
                    ProfileHeaderComposeParams(
                        userName = userName ?: "",
                        profilePicture = profilePicture ?: "",
                    )
                )
//            ProfileCaptionCompose(
//                bio =
//            )
                
            }
        }
    }
}