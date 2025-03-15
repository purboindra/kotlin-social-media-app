package com.example.socialmedia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.imageLoader

@Composable
fun CommentCompose(
    comment: String,
    author: String,
    imageUrl: String,
    userId: String,
    navHostController: NavHostController
) {
    
    val context = LocalContext.current
    
    fun onClickProfile() {
        navHostController.navigate("/profile?userId=${userId}")
    }
    
    
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        ) {
            append(author)
        }
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400
            )
        ) {
            append(" ")
            append(comment)
        }
    }
    
    Row(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .clickable {
                onClickProfile()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(
                imageUrl
            ).crossfade(true).build(),
            contentDescription = author,
            imageLoader = imageLoader(context),
            modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(100)),
            contentScale = ContentScale.Crop
        )
        5.HorizontalSpacer()
        Text(
            annotatedText,
            lineHeight = 15.sp,
            style = MaterialTheme.typography.labelSmall.copy(
            )
        )
    }
}