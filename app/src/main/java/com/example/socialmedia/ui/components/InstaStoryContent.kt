package com.example.socialmedia.ui.components

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun InstaStoryContent(
    modifier: Modifier,
    surfaceRequest: SurfaceRequest? = null
) {
    surfaceRequest?.let {
        val coordinateTransformer = remember {
            MutableCoordinateTransformer()
        }
        Box(
            modifier = modifier
                .background(Color.Red)
        ) {
            CameraXViewfinder(
                surfaceRequest = it,
                coordinateTransformer = coordinateTransformer
            )
        }
    }
}