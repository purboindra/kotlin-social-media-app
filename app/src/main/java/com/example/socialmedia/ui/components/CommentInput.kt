package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun CommentInput(
    postViewModel: PostViewModel,
    id: String,
    modifier: Modifier,
) {
    
    var commentText by remember { mutableStateOf("") }
    
    TextField(
        value = commentText,
        onValueChange = {
            commentText = it
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
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
            .heightIn(min = 0.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        suffix = {
            IconButton(
                onClick = {
                    postViewModel.sendComment(id = id, commentText)
                    commentText = ""
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    )
}
