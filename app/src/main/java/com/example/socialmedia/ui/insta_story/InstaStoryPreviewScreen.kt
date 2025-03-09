package com.example.socialmedia.ui.insta_story

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.PermissionHelper
import com.example.socialmedia.utils.PostHelper
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun InstaStoryPreviewScreen(
    videoUri: Uri?,
    navHostController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    
    val permissionGranted = remember { mutableStateOf(false) }
    var loadingPermission by remember { mutableStateOf(false) }
    val selectedImage by postViewModel.image.collectAsState()
    val images by postViewModel.images.collectAsState()
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionGranted.value = permissions.all {
                it.value
            }
            if (permissionGranted.value) {
                val galleryImages = PostHelper.getGalleryImages(context)
                postViewModel.addImage(galleryImages)
            } else {
                Toast.makeText(
                    context,
                    "Permissions denied. Cannot load gallery images.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
    
    val requiredPermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
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
                requiredPermissions
            )
        } else {
            permissionGranted.value = true
            val galleryImages = PostHelper.getGalleryImages(context)
            galleryImages.forEach {
                PostHelper.scanMediaFile(context, it)
            }
            postViewModel.addImage(galleryImages)
        }
        loadingPermission = false
    }
    
    
    Box(modifier = Modifier.fillMaxSize()) {
        
        if (loadingPermission) Text("Loading...") else Column {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Image")
            }
            10.VerticalSpacer()
            AppElevatedButton(
                onClick = {
                    navHostController.previousBackStackEntry?.savedStateHandle?.set(
                        "videoUri", videoUri?.toString()
                    )
                    navHostController.popBackStack()
                },
                text = "Post",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    
}