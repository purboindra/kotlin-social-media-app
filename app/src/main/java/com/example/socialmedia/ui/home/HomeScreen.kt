package com.example.socialmedia.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryCompose
import com.example.socialmedia.ui.components.LoadingPostCard
import com.example.socialmedia.ui.components.PostCardCompose
import com.example.socialmedia.ui.viewmodel.InstastoryViewModel
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.example.socialmedia.utils.imageLoader
import kotlinx.coroutines.launch
import shimmerLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
    instaStoryViewModel: InstastoryViewModel = hiltViewModel(),
) {
    
    val horizontalPadding = 8.dp
    val postState by postViewModel.postState.collectAsState()
    val userId by instaStoryViewModel.userId.collectAsState()
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    
    var imageUrl by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val clipboardManager = LocalClipboardManager.current
    
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    LaunchedEffect(isLoaded) {
        if (!isLoaded) {
            instaStoryViewModel.getUserId(context)
            postViewModel.fetchAllPosts()
            instaStoryViewModel.fetchAllInstastories()
            isLoaded = true
        }
    }
    
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    5.HorizontalSpacer()
                    Text(
                        "Share",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
                8.VerticalSpacer()
                Box(
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth()
                        .padding(horizontalPadding)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image Post",
                        loading = {
                            Box(modifier = Modifier.shimmerLoading())
                        },
                        imageLoader = imageLoader(context),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                15.VerticalSpacer()
                Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = {
                                val annotatedString = buildAnnotatedString {
                                    append("Hello World!")
                                }
                                clipboardManager.setText(annotatedString)
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            },
                        ) {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = "Copy Link",
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        Text("Copy Link")
                    }
                }
            }
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        item {
            HomeScreenHeader(
                navController,
                instaStoryViewModel,
                userId = userId ?: ""
            )
            10.VerticalSpacer()
        }
        item {
            InstaStoryCompose(
                navHostController = navController,
                instastoryViewModel = instaStoryViewModel
            )
            10.VerticalSpacer()
        }
        when (postState) {
            is State.Success -> {
                val items = (postState as State.Success).data
                if (items.isNotEmpty()) {
                    items(items, key = {
                        it.id
                    }) { item ->
                        PostCardCompose(
                            horizontalPadding,
                            item,
                            postViewModel = postViewModel,
                            navHostController = navController,
                            onShareAction = { image ->
                                showBottomSheet = true
                                imageUrl = image
                            }
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No posts found")
                        }
                    }
                }
            }
            
            is State.Loading -> {
                items(4) {
                    LoadingPostCard()
                }
            }
            
            is State.Failure -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error: ${(postState as State.Failure).throwable.message}")
                    }
                }
            }
            
            else -> {}
        }
    }
}