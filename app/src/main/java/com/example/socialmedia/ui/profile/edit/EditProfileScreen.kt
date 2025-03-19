package com.example.socialmedia.ui.profile.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.components.AppSnackbar
import com.example.socialmedia.ui.viewmodel.EditProfileViewModel
import com.example.socialmedia.ui.viewmodel.SnackbarViewModel
import com.example.socialmedia.utils.VerticalSpacer
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    userId: String,
    navHostController: NavHostController,
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    
    val userState by editProfileViewModel.userState.collectAsState()
    val username by editProfileViewModel.userNameState.collectAsState()
    val bio by editProfileViewModel.bioState.collectAsState()
    val updatedUserState by editProfileViewModel.updatedUserState.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarConfig by snackbarViewModel.snackbarState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        editProfileViewModel.fetchUserById(userId)
    }
    
    LaunchedEffect(updatedUserState) {
        if (updatedUserState is State.Success) {
            coroutineScope.launch {
                snackbarViewModel.showSnackbar(
                    "Successfully Edit Profile",
                    isError = false
                )
            }
            navHostController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("profileUpdated", true)
            navHostController.popBackStack()
            
        } else if (updatedUserState is State.Failure) {
            val message = (updatedUserState as State.Failure).throwable.message
            coroutineScope.launch {
                snackbarViewModel.showSnackbar(
                    message ?: "Unknown Error Occurred",
                    isError = true
                )
            }
        }
    }
    
    Scaffold(
        snackbarHost = {
            AppSnackbar(snackbarHostState, snackbarConfig)
        },
        bottomBar = {
        if (userState is State.Success) {
            AppElevatedButton(
                onClick = {
                    editProfileViewModel.updatedUser(userId)
                },
                enabled = updatedUserState !is State.Loading,
                text = if (updatedUserState is State.Loading) "Loading..." else
                    "Save",
                modifier = Modifier
                    .fillMaxWidth()
                    .safeContentPadding()
            )
        }
    }) { paddingValues ->
        when (userState) {
            is State.Success -> {
                val data = (userState as State.Success).data
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Edit Profile",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }
                        10.VerticalSpacer()
                        Box(
                            modifier = Modifier
                                .height(84.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = data.profilePicture,
                                contentDescription = data.username,
                                modifier = Modifier
                                    .width(84.dp)
                                    .fillMaxHeight()
                                    .clip(
                                        RoundedCornerShape(100)
                                    ),
                                contentScale = ContentScale.Crop,
                                
                                )
                        }
                        5.VerticalSpacer()
                        Text(
                            "Username",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                        AppOutlinedTextField(
                            query = username,
                            onValueChange = {
                                editProfileViewModel.onUserNameChange(it)
                            },
                        )
                        5.VerticalSpacer()
                        Text(
                            "Bio",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                        AppOutlinedTextField(
                            query = bio,
                            onValueChange = {
                                editProfileViewModel.onBioChange(it)
                            },
                            maxLines = 4,
                            singleLine = false
                        )
                    }
                }
            }
            
            is State.Failure -> {
                val message = (userState as State.Failure).throwable.message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        message ?: "Unknown Error",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Red,
                        )
                    )
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
}