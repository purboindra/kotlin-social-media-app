package com.example.socialmedia.ui.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.viewmodel.MessageViewModel
import com.example.socialmedia.utils.HorizontalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessageScreen(
    user: UserModel,
    navHostController: NavHostController,
    messageViewModel: MessageViewModel = hiltViewModel()
) {
    
    val messageQuery by messageViewModel.message.collectAsState()
    val messagesState by messageViewModel.messagesState.collectAsState()
    
    LaunchedEffect(Unit) {
        messageViewModel.fetchMessages()
        messageViewModel.subscribeToMessages("1")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Row() {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                        ) {
                            AsyncImage(
                                model = user.profilePicture ?: "",
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(100)
                                    ),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            )
                        }
                        18.HorizontalSpacer()
                        Column {
                            Text(
                                user.fullName ?: "-",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                            Text(
                                "@${user.username}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color.Gray,
                                )
                            )
                            
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    AppOutlinedTextField(
                        query = messageQuery,
                        onValueChange = {
                            messageViewModel.onMessageChange(it)
                        },
                        placeholderText = "Type a message...",
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                    10.HorizontalSpacer()
                    IconButton(onClick = {
                        messageViewModel.sendMessage(user.id)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(32.dp),
                            tint = BlueLight
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            items(10) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth(),
                        contentAlignment = if (it % 2 == 0) Alignment.TopStart else Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (it % 2 == 0) Color.DarkGray else BluePrimary,
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.45f),
                                horizontalAlignment = Alignment.End,
                            ) {
                                Text(
                                    "Message Message Message Message",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                    )
                                )
                                Row(
                                    horizontalArrangement = if (it % 2 == 0) Arrangement.End else Arrangement.Start,
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        "21:23",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White
                                        )
                                    )
                                    3.HorizontalSpacer()
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Message Sent",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}