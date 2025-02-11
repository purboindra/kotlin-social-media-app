package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.imageLoader

@Composable
fun PostAuthorCompose(
    profilePicture:String,
    userName:String,
    fullName:String,
) {
    val context = LocalContext.current
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(100))
                .background(
                    Color.LightGray
                )
        ) {
            AsyncImage(
                model = profilePicture,
                contentDescription = userName,
                imageLoader = imageLoader(context),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        5.HorizontalSpacer()
        Text(
            fullName,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp
            )
        )
    }
}