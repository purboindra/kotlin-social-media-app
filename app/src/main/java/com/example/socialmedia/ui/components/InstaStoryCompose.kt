package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.InstastoryViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun InstaStoryCompose(
    instastoryViewModel: InstastoryViewModel,
    navHostController: NavHostController,
) {
    val instastoriesState by instastoryViewModel.instastoriesState.collectAsState()
    val currentUserId by instastoryViewModel.currentUserId.collectAsState()
    
    val colorStops = arrayOf(
        0.0f to Color.Yellow,
        0.5f to Color.Red,
        1f to Color.Red,
    )
    
    fun navigate(
        hasInstastory: Boolean,
        contentUrl: String,
        profilePicture: String
    ) {
        if (hasInstastory) {
            navHostController.navigate("instastory_preview_screen?imageUrl=${contentUrl}&profilePicture=${profilePicture}")
        } else {
            navHostController.navigate("instastory_screen?imageUri=null")
        }
    }
    
    when (instastoriesState) {
        is State.Success -> {
            val instatories =
                (instastoriesState as State.Success<List<InstaStory>>).data
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .padding(horizontal = 8.dp)
            ) {
                items(instatories) { instaStory ->
                    
                    val userName =
                        if (instaStory.id == currentUserId) "You" else instaStory.fullName
                    
                    Column(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .width(92.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        ) {
                            if (instaStory.instastories.isNotEmpty()) Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(shape = RoundedCornerShape(100))
                                    .background(
                                        Brush.horizontalGradient(colorStops = colorStops)
                                    )
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(shape = RoundedCornerShape(100))
                                    .background(Color.White)
                                    .align(Alignment.Center)
                                    .clickable {
                                        navigate(
                                            instaStory.instastories.isNotEmpty(),
                                            if (instaStory.instastories.isNotEmpty()) instaStory.instastories.first().contentUrl else "",
                                            instaStory.profilePicture
                                        )
                                    },
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(shape = RoundedCornerShape(100))
                                    .background(GrayPrimary)
                                    .align(Alignment.Center)
                                    .clickable {
                                        navigate(
                                            instaStory.instastories.isNotEmpty(),
                                            if (instaStory.instastories.isNotEmpty()) instaStory.instastories.first().contentUrl else "",
                                            instaStory.profilePicture
                                        )
                                    },
                            ) {
                                AsyncImage(
                                    model = instaStory.profilePicture,
                                    contentDescription = instaStory.fullName,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            
                            if (instaStory.id == currentUserId) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(shape = RoundedCornerShape(100))
                                        .align(Alignment.BottomEnd)
                                        .clickable {
                                            navigate(
                                                instaStory.instastories.isNotEmpty(),
                                                if (instaStory.instastories.isNotEmpty()) instaStory.instastories.first().contentUrl else "",
                                                instaStory.profilePicture
                                            )
                                        },
                                ) {
                                    if (instaStory.instastories.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(
                                                    shape = RoundedCornerShape(
                                                        100
                                                    )
                                                )
                                                .background(Color.White)
                                                .align(Alignment.BottomEnd)
                                        )
                                    }
                                    
                                    if (instaStory.instastories.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .offset(x = 7.dp, y = 7.dp)
                                                .clip(
                                                    shape = RoundedCornerShape(
                                                        100
                                                    )
                                                )
                                                .background(BlueLight)
                                        ) {
                                            Icon(
                                                Icons.Outlined.Add,
                                                contentDescription = "Add",
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .offset(
                                                        x = 3.dp, y = 3.dp
                                                    ),
                                                tint = Color.White,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        5.VerticalSpacer()
                        Text(
                            userName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 13.sp,
                            )
                        )
                    }
                }
            }
        }
        
        else -> {}
    }
    
    
}