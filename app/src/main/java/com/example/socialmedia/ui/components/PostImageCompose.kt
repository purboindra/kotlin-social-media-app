package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.utils.imageLoader

@Composable
fun PostImageCompose(
    imageUrl:String,
    caption:String,
) {
    val context = LocalContext.current
    
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = caption,
        imageLoader = imageLoader(context),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}