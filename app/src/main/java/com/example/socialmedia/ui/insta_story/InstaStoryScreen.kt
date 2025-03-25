package com.example.socialmedia.ui.insta_story

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.components.CloseIconCompose
import com.example.socialmedia.ui.components.GalleryBottomSheet
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.example.socialmedia.ui.viewmodel.InstastoryViewModel
import com.example.socialmedia.utils.FileHelper
import com.example.socialmedia.utils.PermissionHelper
import com.example.socialmedia.utils.PostHelper
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstaStoryScreen(
    navHostController: NavHostController,
    instaStoryViewModel: InstastoryViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val permissionGranted = remember { mutableStateOf(false) }
    var loadingPermission by remember { mutableStateOf(false) }
    
    val images by instaStoryViewModel.images.collectAsState()
    val selectedImage by instaStoryViewModel.image.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    /// CAMERA
    val surfaceRequest by cameraViewModel.surfaceRequest.collectAsState()
    val isLaodingBindCamera by cameraViewModel.isLoadingBindCamera.collectAsState()
    var autoFocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val isFlashOn by cameraViewModel.isFlashOn.collectAsState()
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionGranted.value = permissions.all {
                it.value
            }
            if (!permissionGranted.value) {
                Toast.makeText(
                    context,
                    "Permissions denied. Cannot take a video.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val galleryImages = PostHelper.getGalleryImages(context)
                instaStoryViewModel.addImages(galleryImages)
            }
        }
    )
    
    val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    
    LaunchedEffect(Unit) {
        loadingPermission = true
        if (!PermissionHelper.hasMediaPermissions(context)) {
            permissionLauncher.launch(
                requiredPermission
            )
        } else {
            val galleryImages = PostHelper.getGalleryImages(context)
            galleryImages.forEach {
                PostHelper.scanMediaFile(context, it)
            }
            instaStoryViewModel.addImages(galleryImages)
        }
        loadingPermission = false
    }
    
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context, lifecycleOwner)
    }
    
    val thumbnailGalleryImage = if (images.isNotEmpty()) {
        images.first()
    } else {
        Uri.EMPTY
    }
    
    Scaffold(bottomBar = {
        BottomInstastoryCompose(
            selectedImage = thumbnailGalleryImage ?: Uri.EMPTY,
            navHostController = navHostController,
            onImageTap = {
                showBottomSheet = true
            },
            onCapture = {
                cameraViewModel.imageCapture?.let { imageCapture ->
                    FileHelper.takePicture(
                        imageCapture = imageCapture,
                        onSuccess = {
                            instaStoryViewModel.selectImage(it)
                        },
                        onError = {},
                        context,
                    )
                }
            },
            onSwitch = {
                coroutineScope.launch {
                    cameraViewModel.switchCamera(context, lifecycleOwner)
                }
            },
            image = selectedImage,
            isFlashOn = isFlashOn,
            onToggleFlash = {
                cameraViewModel.toggleFlashLight()
            }
        
        )
    }) { paddingValues ->
        if (showBottomSheet) {
            GalleryBottomSheet(
                sheetState = sheetState,
                setShowBottomSheet = { showBottomSheet = it },
                images = images,
                selectedImage = selectedImage,
                instaStoryViewModel = instaStoryViewModel
            )
        }
        
        if (loadingPermission) Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = BluePrimary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(50.dp)
            )
        } else if (images.isEmpty()) Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("No Images Found")
        } else Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray),
            ) {
                BodyImageCompose(
                    cameraViewModel = cameraViewModel,
                    isLaodingBindCamera = isLaodingBindCamera,
                    image = selectedImage,
                    setAutoFocusRequest = { autoFocusRequest = it },
                    surfaceRequest = surfaceRequest,
                )
                
                selectedImage?.let {
                    CloseIconCompose(
                        modifier = Modifier
                            .fillMaxHeight()
                            .safeContentPadding()
                            .align(Alignment.TopEnd),
                        onClick =
                        {
                            /// LOGIC IF IMAGE WAS TAKEN BY CAMERA
                            coroutineScope.launch {
                                cameraViewModel.restartCamera(
                                    context,
                                    lifecycleOwner
                                )
                            }
                            
                            /// LOGIC IF IMAGE WAS SELECTED FROM GALLERY
                            selectedImage?.let {
                                instaStoryViewModel.selectImage(null)
                            }
                        }
                    )
                }
            }
        }
    }
}