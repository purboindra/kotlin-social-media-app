package com.example.socialmedia.ui.insta_story

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun BottomInstastoryCompose(
    selectedImage: Uri,
    navHostController: NavHostController,
    onClick:()->Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .safeContentPadding().clickable {
                onClick()
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        selectedImage
                    ),
                    contentDescription = "Insta Story Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            8.HorizontalSpacer()
            AppElevatedButton(
                onClick = {
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("imageUri", selectedImage.toString())
                    
                    navHostController.popBackStack()
                    
                },
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                text = "Upload"
            )
        }
    }
}