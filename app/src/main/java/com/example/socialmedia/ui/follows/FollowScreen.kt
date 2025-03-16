package com.example.socialmedia.ui.follows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.viewmodel.FollowsViewModel
import com.example.socialmedia.utils.ConnectionType
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun FollowScreen(
    userId: String,
    type: ConnectionType,
    navHostController: NavHostController,
    followsViewModel: FollowsViewModel = hiltViewModel()
) {
    
    val followsState by followsViewModel.followsState.collectAsState()
    
    LaunchedEffect(Unit) {
        followsViewModel.invokeFollows(userId, type)
    }
    
    Scaffold { paddingValues ->
        when (followsState) {
            is State.Success -> {
                val data = (followsState as State.Success).data
                Column(modifier = Modifier.padding(paddingValues)) {
                    AppOutlinedTextField(
                        query = "",
                        onValueChange = {}
                    )
                    5.VerticalSpacer()
                    LazyColumn {
                        items(data){
                            item ->   Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.userFollow.profilePicture,
                                contentDescription = item.userFollow.username,
                                modifier = Modifier.clip(
                                    CircleShape
                                ),
                                contentScale = ContentScale.Crop
                            )
                            5.HorizontalSpacer()
                            Column {
                                Text(
                                    item.userFollow.username ?: "",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        }
                    }
                }
            }
            
            is State.Failure -> {
                val message = (followsState as State.Failure).throwable.message
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
}