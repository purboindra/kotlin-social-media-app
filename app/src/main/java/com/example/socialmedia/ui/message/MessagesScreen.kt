package com.example.socialmedia.ui.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.viewmodel.MessageViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun MessagesScreen(
    messageViewModel: MessageViewModel = hiltViewModel()
) {
    
    val messagesState by messageViewModel.messagesState.collectAsState()
    
    LaunchedEffect(Unit) {
        messageViewModel.fetchMessages()
    }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Message", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }
            10.VerticalSpacer()
            when (messagesState) {
                is State.Success -> {
                    val messages = (messagesState as State.Success).data
                    if (messages.isEmpty()) Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Message Yet")
                    } else {
                        LazyColumn {
                            items(messages) { message ->
                                Text(message.senderId)
                            }
                        }
                    }
                }
                
                is State.Failure -> {
                    val message =
                        (messagesState as State.Failure).throwable.message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(message ?: "Unknown Error Occurred")
                    }
                }
                
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}