package com.example.socialmedia.ui.add_post

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.AppSnackbar
import com.example.socialmedia.ui.components.AppTopBar
import com.example.socialmedia.ui.components.IconWithTextHorizontal
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.ui.viewmodel.SharedFileViewModel
import com.example.socialmedia.ui.viewmodel.SnackbarViewModel
import com.example.socialmedia.utils.VerticalSpacer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCaptionScreen(
    navHostController: NavHostController,
    sharedFileViewModel: SharedFileViewModel,
    postViewModel: PostViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val videoUri by sharedFileViewModel.videoUri.collectAsState()
    
    val imageUri =
        navHostController.currentBackStackEntry?.arguments?.getString("imageUri")
    val caption by postViewModel.caption.collectAsState()
    val createPostState by postViewModel.createPostState.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarConfig by snackbarViewModel.snackbarState.collectAsState()
    
    Log.d("Create Caption Screen","Video available: ${videoUri?.path}")
    
    LaunchedEffect(createPostState) {
        when (createPostState) {
            is State.Success -> {
                coroutineScope.launch {
                    snackbarViewModel.showSnackbar(
                        "Post created successfully",
                        isError = false
                    )
                }
                navHostController.navigate(Screens.Main.route) {
                    popUpTo(navHostController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
            
            is State.Failure -> {
                val message =
                    (createPostState as State.Failure).throwable.message
                        ?: "Something went wrong"
                coroutineScope.launch {
                    snackbarViewModel.showSnackbar(message, isError = false)
                }
            }
            
            else -> {}
        }
    }
    
    LaunchedEffect(snackbarConfig) {
        snackbarConfig?.let {
            snackbarHostState.showSnackbar(it.message, duration = it.duration)
        }
    }
    
    Scaffold(
        snackbarHost = {
            AppSnackbar(snackbarHostState, snackbarConfig)
        },
        topBar = {
            AppTopBar(
                title = "New Post",
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            AppElevatedButton(
                onClick = {
                    postViewModel.createPost(
                        context,
                        Uri.parse(imageUri)
                    )
                },
                enabled = createPostState !is State.Loading,
                text = "Post",
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier
            .safeDrawingPadding()
            .padding(horizontal = 8.dp)
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                imageUri?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(it)),
                            contentDescription = "Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                    }
                }
                TextField(
                    value = caption,
                    onValueChange = { postViewModel.onChangeCaption(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 10,
                    minLines = 5,
                    singleLine = false,
                    placeholder = {
                        Text(
                            "Add caption...",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrayDark,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = BluePrimary,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = BluePrimary,
                        disabledTextColor = Color.Black,
                    )
                )
                5.VerticalSpacer()
                IconWithTextHorizontal(
                    icon = Icons.Outlined.Person,
                    label = "Tag friends",
                    iconDescription = "Tag friends",
                    onClick = {}
                )
                8.VerticalSpacer()
                IconWithTextHorizontal(
                    icon = Icons.Outlined.LocationOn,
                    label = "Location",
                    iconDescription = "Tag Location",
                    onClick = {}
                )
            }
        }
    }
}