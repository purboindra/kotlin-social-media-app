package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun LikeButton(
    postModel:PostModel,
    postViewModel: PostViewModel,
) {
    IconButton(
        onClick = {
            postViewModel.invokeLike(postModel.id)
        },
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            if (postModel.hasLike == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like",
            tint = Color.Red,
        )
    }
}