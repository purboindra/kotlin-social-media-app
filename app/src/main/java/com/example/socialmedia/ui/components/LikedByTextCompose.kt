package com.example.socialmedia.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun LikedByTextCompose() {
    val annotatedText = buildAnnotatedString {
        append("Liked by ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W400
            )
        ) {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("purboyndra")
            }
            append(" and ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("905,235 others")
            }
        }
    }

    Text(
        "You liked this post",
        style = MaterialTheme.typography.labelSmall
    )
}