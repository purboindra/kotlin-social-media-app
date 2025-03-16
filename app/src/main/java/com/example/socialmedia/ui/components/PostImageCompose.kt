package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.utils.imageLoader
import shimmerLoading

@Composable
fun PostImageCompose(
    imageUrl:String,
    caption:String,
) {
    val context = LocalContext.current
    
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = caption,
        loading = {
            Box(modifier = Modifier.shimmerLoading())
        },
        imageLoader = imageLoader(context),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}