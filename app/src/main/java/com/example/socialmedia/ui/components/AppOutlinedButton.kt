package com.example.socialmedia.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlinedButton(
    modifier: Modifier,
    roundedShape: Dp? = null,
    content: @Composable (RowScope.() -> Unit),
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(roundedShape ?: 8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        content()
    }
}