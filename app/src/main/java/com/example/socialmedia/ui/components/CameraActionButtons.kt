package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun CameraActionButtons(
    onCapture: () -> Unit,
    onSwitch: () -> Unit,
    onToggleFlash: () -> Unit,
    isFlashOn: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .width(56.dp)
                .clip(
                    RoundedCornerShape(100)
                )
                .background(BluePrimary)
                .clickable {
                    onCapture()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = "Take a Photo",
                tint = Color.White,
            )
        }
        18.HorizontalSpacer()
        Icon(
            Icons.Default.Cameraswitch,
            contentDescription = "Switch Camera",
            tint = Color.White,
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
                .clickable {
                    onSwitch()
                }
        )
        18.HorizontalSpacer()
        Icon(
            if (isFlashOn) Icons.Default.FlashOff else Icons.Default.FlashOn,
            contentDescription = "Toggle Flash",
            tint = Color.White,
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
                .clickable {
                    onToggleFlash()
                }
        )
    }
}