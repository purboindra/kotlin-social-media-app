package com.example.socialmedia.ui.components

import android.content.Context
import android.provider.CalendarContract.Colors
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.socialmedia.R
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.example.socialmedia.utils.imageLoader

@Composable
fun PostCardCompose(
    horizontalPadding: Dp,
    postModel: PostModel,
    postViewModel: PostViewModel,
) {
    
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = horizontalPadding,
                    vertical = 5.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PostAuthorCompose(
                userName = postModel.user.username ?: "-",
                profilePicture = postModel.user.profilePicture ?: "",
                fullName = postModel.user.fullName ?: "-",
            )
            MoreButton()
        }
        4.VerticalSpacer()
        Box(
            modifier = Modifier
                .height(375.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            PostImageCompose(
                imageUrl = postModel.imageUrl ?: "",
                caption = postModel.caption
            )
        }
        
        8.VerticalSpacer()
        
        Column(
            modifier = Modifier
                .padding(
                    horizontal = horizontalPadding
                )
                .fillMaxSize(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (postModel.hasLike != null) LikeButton(
                        postViewModel = postViewModel,
                        postModel = postModel
                    )
                    5.HorizontalSpacer()
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Message,
                            contentDescription = "Message",
                        )
                    }
                    5.HorizontalSpacer()
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    
                    ) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = "Shared",
                        )
                    }
                }
                SavedPostButton(
                    id = postModel.id,
                    postViewModel = postViewModel,
                    hasSaved = postModel.hasSaved
                )
            }
            5.VerticalSpacer()
            LikedByTextCompose()
            5.VerticalSpacer()
            ExpandableCaptionCompose(
                text = postModel.caption,
                username = postModel.user.fullName ?: ""
            )
            postModel.comments?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    LazyColumn {
                        items(postModel.comments, key = { it.id })
                        { item ->
                            CommentCompose(
                                comment = item.comment,
                                author = item.user.fullName ?: "-",
                                imageUrl = item.user.profilePicture ?: "",
                            )
                        }
                    }
                }
            }
            CommentInput(
                postViewModel,
                id = postModel.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
            )
        }
    }
}