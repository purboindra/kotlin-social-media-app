package com.example.socialmedia.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.ui.MyIconPack
import com.example.socialmedia.ui.myiconpack.Message
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun PostCardCompose(
    horizontalPadding: Dp,
    postModel: PostModel,
    postViewModel: PostViewModel,
    navHostController: NavHostController,
    onShareAction: (imageUrl: String) -> Unit,
) {
    
    fun onClickProfile() {
        navHostController.navigate("/profile?userId=${postModel.user.id}")
    }
    
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
                onClick = {
                    onClickProfile()
                }
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
                    Image(
                        imageVector = MyIconPack.Message,
                        contentDescription = "Message",
                        modifier = Modifier.size(24.dp)
                    )
                    
                    5.HorizontalSpacer()
                    IconButton(
                        onClick = {
                            onShareAction(postModel.imageUrl ?: "")
                        },
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
            LikedByTextCompose(
                isLiked = postModel.hasLike ?: false
            )
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
                                navHostController = navHostController,
                                userId = item.user.id
                            )
                        }
                    }
                }
            }
            CommentInput(
                postViewModel,
                id = postModel.id,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}