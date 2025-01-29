package com.example.socialmedia.ui.components

import android.content.Context
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
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
    context: Context
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(100))
                        .background(
                            GrayPrimary
                        )
                ) {
                    AsyncImage(
                        model = postModel.user.profilePicture,
                        contentDescription = postModel.user.username,
                        imageLoader = imageLoader(context),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                5.HorizontalSpacer()
                Text(
                    postModel.user.fullName ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp
                    )
                )
            }
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "More",
                modifier = Modifier.size(18.dp)
            )
        }
        
        4.VerticalSpacer()
        
        Box(
            modifier = Modifier
                .height(375.dp)
                .fillMaxWidth()
                .background(GrayDark)
        ) {
            AsyncImage(
                model = postModel.imageUrl,
                contentDescription = postModel.caption,
                imageLoader = imageLoader(context),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        8.VerticalSpacer()
        
        Column(
            modifier = Modifier.padding(
                horizontal = horizontalPadding
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (postModel.hasLike != null) LikeButton(
                        isLiked = postModel.hasLike,
                        onClick = {
                            postViewModel.invokeLike(postModel.id)
                        }
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
                Icon(
                    Icons.Outlined.Save,
                    contentDescription = "Save",
                    modifier = Modifier.size(24.dp)
                )
            }
            5.VerticalSpacer()
            LikedByTextCompose()
            5.VerticalSpacer()
            ExpandableCaptionCompose(
                text = postModel.caption,
            )
        }
    }
}