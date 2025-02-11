package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun CommentInput(
    postViewModel: PostViewModel,
    id: String,
    modifier: Modifier,
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
        shape = RoundedCornerShape(
            12.dp
        ),
        modifier = modifier
            .fillMaxWidth().padding(vertical = 1.dp),
        suffix = {
            IconButton(
                onClick = {
                    postViewModel.sendComment(id = id)
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Send",
                    modifier = modifier.size(18.dp)
                )
            }
        }
    )
}
