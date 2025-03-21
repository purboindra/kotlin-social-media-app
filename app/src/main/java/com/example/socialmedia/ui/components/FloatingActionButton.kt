package com.example.socialmedia.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppFloatingActionButton(
    onClick: () -> Unit,
    imageVector: ImageVector? = null,
    contentDescription: String,
) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(imageVector ?: Icons.Filled.Add, contentDescription)
    }
}