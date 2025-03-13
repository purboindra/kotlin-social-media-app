package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun TabContent(
    imageVector: ImageVector,
    contentDescription: String,
    hasSelected: Boolean,
    onClick: () -> Unit,
) {
    Tab(
        selected = hasSelected,
        onClick = onClick,
        modifier = Modifier
            .height(84.dp)
            .width(84.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector,
                contentDescription,
                modifier = Modifier.size(32.dp)
            )
            5.VerticalSpacer()
            if (hasSelected) Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(64.dp)
                    .background(Color.Black)
            )
        }
    }
}