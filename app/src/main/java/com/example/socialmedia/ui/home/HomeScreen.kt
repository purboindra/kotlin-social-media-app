package com.example.socialmedia.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryCompose
import com.example.socialmedia.ui.components.PostCardCompose
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun HomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    
    val horizontalPadding = 8.dp
    
    val context = LocalContext.current
    
    val postState by postViewModel.postState.collectAsState()
    
    LaunchedEffect(Unit) {
        postViewModel.fetchAllPosts()
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreenHeader()
        10.VerticalSpacer()
        LazyColumn {
            item {
                InstaStoryCompose()
                10.VerticalSpacer()
            }
            when (postState) {
                is State.Success -> {
                    val items = (postState as State.Success).data
                    if (items.isNotEmpty()) {
                        items(items) { item ->
                            PostCardCompose(
                                horizontalPadding,
                                item,
                                context = context
                            )
                            18.VerticalSpacer()
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No posts found")
                            }
                        }
                    }
                }
                
                is State.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                
                is State.Failure -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error: ${(postState as State.Failure).throwable.message}")
                        }
                    }
                }
                
                else -> {}
            }
        }
    }
}