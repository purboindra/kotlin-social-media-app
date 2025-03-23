package com.example.socialmedia.ui.message

import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.ui.components.AppFloatingActionButton
import com.example.socialmedia.ui.components.dialog.AppDialogUsers
import com.example.socialmedia.ui.viewmodel.MessageViewModel
import com.example.socialmedia.ui.viewmodel.SearchViewModel
import com.example.socialmedia.utils.VerticalSpacer
import kotlinx.serialization.json.Json

@Composable
fun MessagesScreen(
    messageViewModel: MessageViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    
    val messagesState by messageViewModel.messagesState.collectAsState()
    val searchState by searchViewModel.searchState.collectAsState()
    val query by searchViewModel.queryState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserModel?>(null) }
    
    LaunchedEffect(Unit) {
        messageViewModel.fetchMessages()
    }
    
    LaunchedEffect(selectedUser) {
        selectedUser?.let {
            val jsonUser = Uri.encode(Json.encodeToString(it))
            navHostController.navigate("direct_message?user=${jsonUser}")
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            Log.d("MessageScreen", "Unsubscribing to message")
            messageViewModel.unSubscribeToMessage()
        }
    }
    
    when {
        openAlertDialog.value -> {
            AppDialogUsers(
                query = query,
                loading = searchState is State.Loading,
                onValueChange = {
                    searchViewModel.onChangeQuery(
                        it
                    )
                },
                users = if (searchState is State.Success) (searchState as State.Success).data else emptyList(),
                onTap = {
                    Log.d("Dialog User", "User: ${it.fullName}")
                    selectedUser = it
                },
                onDismissRequest = {
                    openAlertDialog.value = !openAlertDialog.value
                }
            )
        }
    }
    
    Scaffold(
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = {
                    openAlertDialog.value = true
                },
                contentDescription = "Add Message"
            )
        }
    ) { paddingValues ->
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