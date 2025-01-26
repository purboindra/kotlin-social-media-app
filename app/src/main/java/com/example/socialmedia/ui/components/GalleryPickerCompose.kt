package com.example.socialmedia.ui.components

import android.graphics.Paint.Align
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.AddPostViewModel
import com.example.socialmedia.utils.PermissionHelper
import com.example.socialmedia.utils.PostHelper

@Composable
fun GalleryPickerCompose(
    addPostViewModel: AddPostViewModel
) {
    val context = LocalContext.current
    val images = remember { mutableStateListOf<Uri>() }
    val permissionGranted = remember { mutableStateOf(false) }
    var loadingPermission by remember { mutableStateOf(false) }
    val selectedImage by addPostViewModel.image.collectAsState()
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionGranted.value = permissions.all {
                it.value
            }
            if (permissionGranted.value) {
                val galleryImages = PostHelper.getGalleryImages(context)
                images.addAll(galleryImages)
            } else {
                Toast.makeText(
                    context,
                    "Permissions denied. Cannot load gallery images.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
    
    LaunchedEffect(Unit) {
        loadingPermission = true
        if (!PermissionHelper.hasMediaPermissions(context)) {
            permissionLauncher.launch(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        android.Manifest.permission.READ_MEDIA_IMAGES,
                        android.Manifest.permission.READ_MEDIA_VIDEO
                    )
                } else {
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            )
        } else {
            permissionGranted.value = true
            val galleryImages = PostHelper.getGalleryImages(context)
            images.addAll(galleryImages)
        }
        loadingPermission = false
    }
    
    if (loadingPermission) Text("Loading....") else Column(
        modifier = Modifier
            .height(420.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1500.dp)
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
                                addPostViewModel.selectImage(imageUri)
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