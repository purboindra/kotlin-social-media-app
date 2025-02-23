package com.example.socialmedia.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.icons.Message
import com.example.socialmedia.icons.message.Message
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.viewmodel.HomeViewModel
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun HomeScreenHeader(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel
) {
    
    val navBackStackEntry = navHostController.currentBackStackEntry
    val videoUri = navBackStackEntry?.savedStateHandle?.get<String>("videoUri")
    
    val context = LocalContext.current
    
    LaunchedEffect(videoUri) {
        videoUri?.let {
            Log.d("HomeScreenHeader", "Video uri: $it")
            val parseVideoUri = Uri.parse(it)
            homeViewModel.createInstaStory(parseVideoUri, context)
        }
    }
    
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        SvgImage(
            svgResource = R.raw.instagram_horizontal_logo,
            contentDescription = "Instagram Logo",
            modifier = Modifier.height(48.dp)
        )
        Row {
            Icon(
                Icons.Outlined.AddBox,
                contentDescription = "Add Post",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navHostController.navigate(Screens.InstaStoryScreen.route)
                    }
            )
            8.HorizontalSpacer()
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(32.dp)
            
            )
            8.HorizontalSpacer()
            Image(
                Message.Message,
                contentDescription = "Direct Message",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}