package com.example.socialmedia.ui.insta_story

import android.Manifest
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.components.InstaStoryContent
import com.example.socialmedia.ui.viewmodel.CameraViewModel
import com.example.socialmedia.utils.PermissionHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InstaStoryScreen(
    navHostController: NavHostController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraViewModel: CameraViewModel = hiltViewModel()
) {
    
    val context = LocalContext.current
    val permissionGranted = remember { mutableStateOf(false) }
    var loadingPermission by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()
    
    val surfaceRequest by cameraViewModel.surfaceRequest.collectAsState()
    
    var isRecording by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(lifecycleOwner) {
        cameraViewModel.bindToCameraInstaStory(context, lifecycleOwner)
    }
    
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
            }
        }
    )
    
    val requiredPermission = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            )
        }
        
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            )
        }
        
        else -> {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }
    
    LaunchedEffect(Unit) {
        loadingPermission = true
        if (!PermissionHelper.hasMediaPermissions(context)) {
            permissionLauncher.launch(
                requiredPermission
            )
        }
        
        loadingPermission = false
    }
    
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            
            InstaStoryContent(
                modifier = Modifier.fillMaxSize(),
                surfaceRequest = surfaceRequest
            )
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navHostController.popBackStack()
                        }
                )
                
                Icon(
                    Icons.Outlined.FlashOn,
                    contentDescription = "Flash",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 64.dp, y = 5.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .width(56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Cameraswitch,
                        contentDescription = "Switch Camera",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                coroutineScope.launch {
                                    cameraViewModel.switchCamera(
                                        context,
                                        lifecycleOwner
                                    )
                                }
                            },
                        tint = Color.White,
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .width(56.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White)
                        .pointerInteropFilter { event ->
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    Log.d("Insta Story Screen", "onPressed")
                                    cameraViewModel.toggleIsRecording()
                                    cameraViewModel.bindToCameraInstaStory(
                                        context,
                                        lifecycleOwner,
                                    )
                                    true
                                }
                                
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                    cameraViewModel.toggleIsRecording()
                                    Log.d("Insta Story Screen", "onRelease")
                                    /// STOP VIDEO
                                    true
                                }
                                
                                else -> false
                            }
                        },
                )
            }
        }
    }
}