package com.example.socialmedia.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryCompose
import com.example.socialmedia.ui.components.LikedByTextCompose
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun HomeScreen() {

    val horizontalPadding = 8.dp

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreenHeader()
        10.VerticalSpacer()
        LazyColumn {
            item {
                InstaStoryCompose()
                10.VerticalSpacer()
            }
            items(5) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(
                                        BlueLight
                                    )
                            )
                            5.HorizontalSpacer()
                            Text(
                                "Purboyndra", style = MaterialTheme.typography.titleMedium.copy(
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
                    )

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

                            Row {
                                Icon(
                                    Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Like",
                                    modifier = Modifier.size(18.dp)
                                )
                                5.HorizontalSpacer()
                                Icon(
                                    Icons.AutoMirrored.Outlined.Message,
                                    contentDescription = "Message",
                                    modifier = Modifier.size(18.dp)
                                )
                                5.HorizontalSpacer()
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = "Shared",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Icon(
                                Icons.Outlined.Save,
                                contentDescription = "Save",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        5.VerticalSpacer()
                        LikedByTextCompose()
                    }
                }
                18.VerticalSpacer()
            }
        }
    }
}