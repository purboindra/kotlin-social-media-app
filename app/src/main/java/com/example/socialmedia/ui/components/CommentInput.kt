package com.example.socialmedia.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun CommentInput(
    postViewModel: PostViewModel,
) {
    
    val commentText by postViewModel.commentText.collectAsState()
    
    TextField(
        value = commentText,
        onValueChange = {
            postViewModel.onChangeComment(it)
        },
        singleLine = true,
        placeholder = {
            Text(
                "Add a comment",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = GrayDark,
                )
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
    )
}
