package com.example.socialmedia.ui.insta_story

import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import java.util.UUID

@Composable
fun BodyImageCompose(
    isLaodingBindCamera: Boolean,
    surfaceRequest: SurfaceRequest? = null,
    image: Uri? = null,
    cameraViewModel: CameraViewModel,
    setAutoFocusRequest: (Pair<UUID, Offset>) -> Unit,
) {

    if (isLaodingBindCamera) Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = BluePrimary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(50.dp)
        )
    } else surfaceRequest?.let { request ->
        val coordinateTransformer = remember {
            MutableCoordinateTransformer()
        }

        if (image == null) CameraXViewfinder(
            surfaceRequest = request,
            coordinateTransformer = coordinateTransformer,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { tapCoords ->
                    with(coordinateTransformer) {
                        cameraViewModel.tapToFocus(tapCoords.transform())
                    }
                    setAutoFocusRequest(
                        UUID.randomUUID() to tapCoords
                    )
                }
            }
        ) else Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = "Insta Story Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Gray)
        )
    }
}