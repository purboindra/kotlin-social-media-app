package com.example.socialmedia.ui.insta_story

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.CameraActionButtons
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun BottomInstastoryCompose(
    selectedImage: Uri,
    navHostController: NavHostController,
    onImageTap: () -> Unit,
    onCapture: () -> Unit,
    onSwitch: () -> Unit,
    onToggleFlash: () -> Unit,
    onZoom: (Float) -> Unit,
    image: Uri? = null,
    isFlashOn: Boolean,
    cameraViewModel: CameraViewModel
) {
    
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val minZoom by cameraViewModel.minZoom.collectAsState()
    val maxZoom by cameraViewModel.maxZoom.collectAsState()

    Box(
        modifier = Modifier
            .safeContentPadding()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            
            Slider(
                value = sliderPosition,
                onValueChange = { value ->
                    sliderPosition = value
                    onZoom(value)
                },
                valueRange = minZoom..maxZoom
            )
            
            8.VerticalSpacer()
            
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
                        CameraActionButtons(
                            onCapture,
                            onSwitch,
                            onToggleFlash,
                            isFlashOn = isFlashOn,
                        )
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
}