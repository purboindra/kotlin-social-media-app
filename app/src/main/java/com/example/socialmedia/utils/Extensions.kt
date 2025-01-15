package com.example.socialmedia.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// SPACING
@Composable
fun Int.VerticalSpacer() {
    Spacer(modifier = Modifier.height(this.dp))
}

@Composable
fun Int.HorizontalSpacer() {
    Spacer(modifier = Modifier.width(this.dp))
}