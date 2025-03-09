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
import androidx.compose.ui.graphics.Color
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

) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)) {
        
        
    }
    
}