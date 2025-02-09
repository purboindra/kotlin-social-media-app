package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreButton(){
    Icon(
        Icons.Filled.MoreVert,
        contentDescription = "More",
        modifier = Modifier.size(18.dp)
    )
}