package com.example.socialmedia.data.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color

data class SnackbarConfig(
    val message: String,
    val backgroundColor: Color,
    val contentColor: Color,
    val duration: SnackbarDuration,
)