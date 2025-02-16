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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryCompose
import com.example.socialmedia.ui.components.LoadingPostCard
import com.example.socialmedia.ui.components.PostCardCompose
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun HomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    
    val horizontalPadding = 8.dp
    
    val postState by postViewModel.postState.collectAsState()
    
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        if (!isLoaded) {
            postViewModel.fetchAllPosts()
            isLoaded = true
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        item {
            HomeScreenHeader(navController)
            10.VerticalSpacer()
        }
        item {
            InstaStoryCompose()
            10.VerticalSpacer()
        }
        when (postState) {
            is State.Success -> {
                val items = (postState as State.Success).data
                if (items.isNotEmpty()) {
                    items(items, key = {
                        it.id
                    }) { item ->
                        PostCardCompose(
                            horizontalPadding,
                            item,
                            postViewModel = postViewModel
                        )
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
                items(4) {
                    LoadingPostCard()
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