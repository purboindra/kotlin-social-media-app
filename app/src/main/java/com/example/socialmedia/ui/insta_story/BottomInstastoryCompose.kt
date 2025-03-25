package com.example.socialmedia.ui.insta_story

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun BottomInstastoryCompose(
    selectedImage: Uri,
    navHostController: NavHostController,
    onImageTap: () -> Unit,
    onCapture: () -> Unit,
    onSwitch: () -> Unit,
    image: Uri? = null,
) {
    Box(
        modifier = Modifier
            .safeContentPadding()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp),
                contentAlignment = Alignment.CenterEnd
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
                        .clickable {
                            onImageTap()
                        }
                )
            }
            8.HorizontalSpacer()
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (image == null) {
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
                    }
                } else {
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
    }
}