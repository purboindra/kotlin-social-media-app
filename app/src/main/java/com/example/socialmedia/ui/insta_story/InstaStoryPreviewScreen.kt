package com.example.socialmedia.ui.insta_story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun InstaStoryPreviewScreen(
    navHostController: NavHostController,
) {
    val imageUrl =
        navHostController.currentBackStackEntry?.arguments?.getString("imageUrl")
    val profilePicture =
        navHostController.currentBackStackEntry?.arguments?.getString("profilePicture")
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        /// CONTENT
        AsyncImage(
            model = imageUrl,
            contentDescription = "Insta Story Preview",
            modifier = Modifier.fillMaxSize()
        )
        
        /// PROFILE PICTUR AND LOADER
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .height(32.dp)
                .fillMaxWidth()
                .offset(y = 42.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    profilePicture,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White)
                )
                5.HorizontalSpacer()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color.White)
                )
            }
        }
        
        Box(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                AppOutlinedTextField(
                    query = "",
                    modifier = Modifier.fillMaxWidth(0.9f),
                    placeholderText = "Write a comment...",
                    onValueChange = {},
                )
                5.HorizontalSpacer()
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp),
                    tint = BlueLight
                )
            }
        }
        
    }
    
}