package com.example.socialmedia.ui.components

import android.text.style.StyleSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun ExpandableCaptionCompose(
    text: String,
    expandedMaxLines: Int = 3,
    username:String,
) {
    
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append("$username ")
        }
        append(text)
    }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = annotatedText,
            maxLines = if (isExpanded) Int.MAX_VALUE else expandedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult.value = it },
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        )
        
        if (!isExpanded && textLayoutResult.value?.hasVisualOverflow == true) {
            Text(
                text = "...more",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable { isExpanded = true }
            )
        }
    }
    
}