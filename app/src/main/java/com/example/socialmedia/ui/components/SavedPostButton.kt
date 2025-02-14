package com.example.socialmedia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun SavedPostButton(
    id: String,
    hasSaved: Boolean,
    postViewModel: PostViewModel
) {
    
    val savedPostState by postViewModel.savedPostState.collectAsState()
    
    Icon(
        Icons.Outlined.Save,
        contentDescription = "Save",
        tint = if(hasSaved) Color.Yellow else Color.Gray,
        modifier = Modifier
            .size(24.dp)
            .clickable {
                postViewModel.savedPost(id)
            }
    )
}