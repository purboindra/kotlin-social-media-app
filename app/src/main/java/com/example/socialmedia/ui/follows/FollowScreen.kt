package com.example.socialmedia.ui.follows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    userId: String,
    type: ConnectionType,
    navHostController: NavHostController,
    followsViewModel: FollowsViewModel = hiltViewModel()
) {
    
    val followsState by followsViewModel.followsState.collectAsState()
    val username by followsViewModel.username.collectAsState()
    
    LaunchedEffect(Unit) {
        followsViewModel.invokeFollows(userId, type)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                Icons.Default.ChevronLeft,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            username ?: "",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        when (followsState) {
            is State.Success -> {
                val data = (followsState as State.Success).data
                Column(modifier = Modifier.padding(paddingValues)) {
                    AppOutlinedTextField(
                        query = "",
                        onValueChange = {},
                        placeholderText = "Search user...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                    5.VerticalSpacer()
                    LazyColumn {
                        items(data) { item ->
                            
                            val user =
                                if (type == ConnectionType.FOLLOWING) item.userFollowed else item.userFollow
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = user.profilePicture,
                                    contentDescription = user.username,
                                    modifier = Modifier.clip(
                                        CircleShape
                                    ),
                                    contentScale = ContentScale.Crop
                                )
                                5.HorizontalSpacer()
                                Column {
                                    Text(
                                        user.username ?: "",
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