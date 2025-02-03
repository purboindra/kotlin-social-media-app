package com.example.socialmedia.ui.camera

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.FileHelper
import com.example.socialmedia.utils.HorizontalSpacer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(
            postViewModel = postViewModel,
            navController = navController
        )
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
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    postViewModel: PostViewModel,
    navController: NavHostController,
) {
    val surfaceRequest by cameraViewModel.surfaceRequest.collectAsState()
    val context = LocalContext.current
    
    val coroutineScope = rememberCoroutineScope()
    
    var autoFocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val autoFocustRequestId = autoFocusRequest.first
    val showAutoFocusIndicator = autoFocusRequest.second.isSpecified
    val autoFocusCoords = remember(autoFocusRequest) {
        autoFocusRequest.second
    }
    
    var isRecording by remember {
        mutableStateOf(false)
    }
    
    val recordedVideoUri by cameraViewModel.recordedVideoUri.collectAsState()
    val videoDuration by cameraViewModel.videoDuration.collectAsState()
    
    val executed = remember {
        Executors.newSingleThreadExecutor()
    }
    
    var capturedImageUrl by remember {
        mutableStateOf<Uri?>(null)
    }
    
    val interactionSource = remember { MutableInteractionSource() }
    
    val viewConfiguration = LocalViewConfiguration.current
    
    if (showAutoFocusIndicator) {
        LaunchedEffect(autoFocustRequestId) {
            delay(1000)
            autoFocusRequest = autoFocustRequestId to Offset.Unspecified
        }
    }
    
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context, lifecycleOwner)
    }
    
    LaunchedEffect(capturedImageUrl) {
        
        if (capturedImageUrl != null) {
            navController.navigate("create_caption?imageUri=${capturedImageUrl.toString()}")
        }
    }
    
    LaunchedEffect(interactionSource) {
        var isLongClick = false
        
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = true
                    Toast.makeText(context, "Long click", Toast.LENGTH_SHORT)
                        .show()
                    cameraViewModel.captureVideo(context)
                    isRecording = true
                }
                
                is PressInteraction.Release -> {
                    isLongClick = false
                    isRecording = false
                    Toast.makeText(context, "click", Toast.LENGTH_SHORT)
                        .show()
                }
                
            }
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
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    Icons.Outlined.Cameraswitch,
                    contentDescription = "Camera Switch",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White,
                )
            }
        }
        
        recordedVideoUri?.let {
            Text("Video saved at: $it", color = Color.White)
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$videoDuration", color = GrayPrimary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .safeContentPadding()
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                IconButton(
                    onClick = {
                        cameraViewModel.imageCapture?.let { imgCapture ->
                            FileHelper.takePicture(
                                imageCapture = imgCapture,
                                context = context,
                                onSave = {
                                    capturedImageUrl = it
                                    postViewModel.selectImage(it)
                                },
                                onError = {
                                    Log.e(
                                        "takePicture",
                                        "Image capture failed: ${it.message}"
                                    )
                                }
                            )
                        }
                    },
                ) {
                    Icon(
                        Icons.Outlined.Camera,
                        contentDescription = "Take Picture",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White,
                    )
                }
                10.HorizontalSpacer()
                
                Button(
                    onClick = {},
                    interactionSource = interactionSource
                ) {
                    Icon(
                        Icons.Outlined.Videocam,
                        contentDescription = "Take Video",
                        modifier = Modifier
                            .size(48.dp),
                        tint = Color.White,
                    )
                }
                
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
