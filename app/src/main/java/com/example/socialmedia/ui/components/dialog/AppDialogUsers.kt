package com.example.socialmedia.ui.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun AppDialogUsers(
    onDismissRequest: () -> Unit,
    onTap: (user: UserModel) -> Unit,
    query: String,
    onValueChange: (query: String) -> Unit,
    users: List<UserModel>,
    loading: Boolean,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                10.VerticalSpacer()
                AppOutlinedTextField(
                    query = query,
                    onValueChange = onValueChange,
                    placeholderText = "Find someone...",
                )
                10.VerticalSpacer()
                if (loading) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        items(users, key = {
                            it.id
                        }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .clickable {
                                        onTap(item)
                                        onDismissRequest()
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.profilePicture,
                                    contentDescription = item.username,
                                    modifier = Modifier.clip(
                                        CircleShape
                                    ),
                                    contentScale = ContentScale.Crop
                                )
                                5.HorizontalSpacer()
                                Column {
                                    Text(
                                        item.username ?: "",
                                        style = MaterialTheme.typography.labelLarge
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

