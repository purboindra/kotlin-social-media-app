package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.imageLoader

@Composable
fun CommentCompose(
    comment: String,
    author: String,
    imageUrl: String,
) {
    
    val context = LocalContext.current
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(
                imageUrl
            ).crossfade(true).build(),
            contentDescription = author,
            imageLoader = imageLoader(context),
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(100)),
            contentScale = ContentScale.Crop
        )
        5.HorizontalSpacer()
        Text(
            author,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        5.HorizontalSpacer()
        Text(
            comment,
            style = MaterialTheme.typography.labelSmall,
        )
    }
    
}