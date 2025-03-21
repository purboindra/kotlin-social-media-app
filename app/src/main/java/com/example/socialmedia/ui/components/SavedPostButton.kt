package com.example.socialmedia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.MyIconPack
import com.example.socialmedia.ui.myiconpack.Save
import com.example.socialmedia.ui.viewmodel.PostViewModel

@Composable
fun SavedPostButton(
    id: String,
    hasSaved: Boolean,
    postViewModel: PostViewModel
) {
    
    Image(
        imageVector = MyIconPack.Save,
        contentDescription = "Save",
        modifier = Modifier
            .size(24.dp)
            .clickable {
                postViewModel.invokeSavedPost(id)
            },
        colorFilter = ColorFilter.tint(if (hasSaved) Color.Red else Color.Gray)
    )

}