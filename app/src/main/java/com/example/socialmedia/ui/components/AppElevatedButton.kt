package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary

@Composable
fun AppElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    contentPadding: PaddingValues? = null
) {
    ElevatedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        contentPadding = contentPadding ?: PaddingValues(vertical = 15.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = BluePrimary,
            contentColor = Color.White,
            disabledContainerColor = BluePrimary.copy(alpha = 0.5f),
            disabledContentColor = GrayPrimary,
        )
    ) {
        Text(text)
    }
}
