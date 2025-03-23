package com.example.socialmedia.ui.insta_story

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.example.socialmedia.ui.viewmodel.InstastoryViewModel
import com.example.socialmedia.utils.FileHelper
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.PermissionHelper
import com.example.socialmedia.utils.PostHelper
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
    
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    /// CAMERA
    val surfaceRequest by cameraViewModel.surfaceRequest.collectAsState()
    val isLaodingBindCamera by cameraViewModel.isLoadingBindCamera.collectAsState()
    var autoFocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var image by rememberSaveable { mutableStateOf<Uri?>(null) }
    
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
            instaStoryViewModel.selectImage(
                galleryImages.firstOrNull() ?: Uri.EMPTY
            )
        }
        loadingPermission = false
    }
    
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCamera(context, lifecycleOwner)
    }
    
    Scaffold { paddingValues ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    columns = GridCells.Adaptive(minSize = 158.dp)
                ) {
                    items(
                        images
                    ) { imageUri ->
                        Box(
                            modifier = Modifier.aspectRatio(1f)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Gallery Image",
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(3.dp)
                                    .clickable {
                                        imageUri?.let {
                                            instaStoryViewModel.selectImage(it)
                                        }
                                    },
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                RadioButton(
                                    selected = selectedImage == imageUri,
                                    onClick = {},
                                    enabled = false,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BluePrimary,
                                        unselectedColor = GrayPrimary,
                                        disabledUnselectedColor = GrayPrimary,
                                        disabledSelectedColor = BlueLight,
                                    )
                                )
                            }
                        }
                    }
                }
            }
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
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            val (x, y) = dragAmount
                            if (y < -50) {
                                showBottomSheet = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                
                BodyImageCompose(
                    cameraViewModel = cameraViewModel,
                    isLaodingBindCamera = isLaodingBindCamera,
                    image = image,
                    setAutoFocusRequest = { autoFocusRequest = it },
                    surfaceRequest = surfaceRequest,
                )
                
                image?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .safeContentPadding()
                            .align(Alignment.TopEnd)
                    ) {
                        IconButton(
                            onClick = {
                                image = null
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

//                if (selectedImage == null) Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        "Swipe up for choose an image",
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            color = Color.White
//                        ),
//                    )
//                }
//                else
//                    Image(
//                        painter = rememberAsyncImagePainter(selectedImage),
//                        contentDescription = "Insta Story Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
                
                BottomInstastoryCompose(
                    selectedImage = images.first()
                        ?: Uri.EMPTY,
                    navHostController = navHostController,
                    onClick = {
                        showBottomSheet = true
                    },
                    onCapture = {
                        cameraViewModel.imageCapture?.let { imageCapture ->
                            FileHelper.takePicture(
                                imageCapture = imageCapture,
                                context,
                                onSave = {
                                    image = it
                                },
                                onError = {},
                            )
                        }
                    },
                    image
                )
            }
        }
    }
}