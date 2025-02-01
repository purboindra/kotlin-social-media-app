package com.example.socialmedia.ui.camera

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(navController: NavHostController) {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent()
    } else {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .wrapContentSize()
                    .widthIn(max = 480.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textToShow =
                    if (cameraPermissionState.status.shouldShowRationale) {
                        // If the user has denied the permission but the rationale can be shown,
                        // then gently explain why the app requires this permission
                        "Whoops! Looks like we need your camera to work our magic!" +
                                "Don't worry, we just wanna see your pretty face (and maybe some cats).  " +
                                "Grant us permission and let's get this party started!"
                    } else {
                        // If it's the first time the user lands on this feature, or the user
                        // doesn't want to be asked again for this permission, explain that the
                        // permission is required
                        "Hi there! We need your camera to work our magic! ✨\n" +
                                "Grant us permission and let's get this party started! \uD83C\uDF89"
                    }
                Text(textToShow, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Unleash the Camera!")
                }
                
            }
        }
    }
}

@Composable
private fun CameraPreviewContent(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val surfaceRequest by cameraViewModel.surfaceRequest.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context, lifecycleOwner)
    }
    val coroutineScope = rememberCoroutineScope()
    
    var autoFocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val autoFocustRequestId = autoFocusRequest.first
    val showAutoFocusIndicator = autoFocusRequest.second.isSpecified
    val autoFocusCoords = remember(autoFocusRequest) {
        autoFocusRequest.second
    }
    
    if (showAutoFocusIndicator) {
        LaunchedEffect(autoFocustRequestId) {
            delay(1000)
            autoFocusRequest = autoFocustRequestId to Offset.Unspecified
        }
    }
    
    surfaceRequest?.let { request ->
        val coordinateTransformer = remember {
            MutableCoordinateTransformer()
        }
        CameraXViewfinder(
            surfaceRequest = request,
            coordinateTransformer = coordinateTransformer,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { tapCoords ->
                    with(coordinateTransformer) {
                        cameraViewModel.tapToFocus(tapCoords.transform())
                    }
                    autoFocusRequest = UUID.randomUUID() to tapCoords
                }
            }
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .safeContentPadding()
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        cameraViewModel.switchCamera(
                            context,
                            lifecycleOwner
                        )
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Icon(
                    Icons.Outlined.Cameraswitch,
                    contentDescription = "Camera Switch",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        
        AnimatedVisibility(
            visible = showAutoFocusIndicator,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset {
                    autoFocusCoords
                        .takeOrElse {
                            Offset.Zero
                        }
                        .round()
                }
                .offset((-24).dp, (-24).dp)
        ) {
            Spacer(
                Modifier
                    .border(2.dp, Color.White, CircleShape)
                    .size(48.dp)
            )
        }
        
    }
}
