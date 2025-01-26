package com.example.socialmedia.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.ExpandableCaptionCompose
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryCompose
import com.example.socialmedia.ui.components.LikedByTextCompose
import com.example.socialmedia.ui.components.PostCardCompose
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun HomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    
    val horizontalPadding = 8.dp
    
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
                    items(5) {
                        PostCardCompose(horizontalPadding)
                        18.VerticalSpacer()
                    }
                }
                
                is State.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
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