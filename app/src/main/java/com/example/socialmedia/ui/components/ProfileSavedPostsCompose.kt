package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.viewmodel.ProfileViewModel
import kotlin.math.ceil

@Composable
fun ProfileSavedPostsCompose(
    profileViewModel: ProfileViewModel,
    userId: String
) {
    
    val savedPostsState by profileViewModel.savedPostsState.collectAsState()
    
    val cardHeight = 96
    val items = List(10) { index -> "Item $index" }
    val rows = ceil(items.size / 3.0).toInt()
    val totalHeight = (rows * cardHeight)
    
    LaunchedEffect(Unit) {
        profileViewModel.fetchSavedPostsByUserId(userId)
    }
    
    when (savedPostsState) {
        is State.Failure -> {
            val message = (savedPostsState as State.Failure).throwable.message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    message ?: "Unknown Error Occurred",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Red
                    )
                )
            }
        }
        
        is State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is State.Success -> {
            val posts = (savedPostsState as State.Success).data
            
            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Post Yet")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(totalHeight.dp)
                ) {
                    itemsIndexed(posts) { _, item ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = item.post.imageUrl,
                                contentDescription = item.post.caption,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
        
        else -> {
            CircularProgressIndicator()
        }
    }
}