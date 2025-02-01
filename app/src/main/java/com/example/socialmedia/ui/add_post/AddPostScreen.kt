package com.example.socialmedia.ui.add_post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.components.GalleryPickerCompose
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    postViewModel: PostViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    
    val image by postViewModel.image.collectAsState()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Create Post")
                },
                actions = {
                    if (image != null) IconButton(onClick = {
                        navHostController.navigate("create_caption?imageUri=${image.toString()}")
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Create Post",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .background(
                        Color.Transparent
                    )
            ) {
                if (image != null) Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp),
                    contentScale = ContentScale.Crop
                ) else Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Choose photo first", modifier = Modifier.clickable {
                        navHostController.navigate(Screens.CameraPreview.route)
                    })
                }
            }
            GalleryPickerCompose(postViewModel)
        }
    }
}