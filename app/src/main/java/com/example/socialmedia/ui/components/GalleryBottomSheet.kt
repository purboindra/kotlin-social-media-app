package com.example.socialmedia.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.InstastoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryBottomSheet(
    sheetState: SheetState,
    setShowBottomSheet: (Boolean) -> Unit,
    images: List<Uri?>,
    selectedImage: Uri?,
    instaStoryViewModel: InstastoryViewModel
) {
    ModalBottomSheet(
        onDismissRequest = {
            setShowBottomSheet(false)
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
                            onClick = {
                                imageUri?.let {
                                    instaStoryViewModel.selectImage(it)
                                }
                            },
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