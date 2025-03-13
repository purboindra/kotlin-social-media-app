package com.example.socialmedia.ui.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppOutlinedButton
import com.example.socialmedia.ui.components.ProfileHeaderCompose
import com.example.socialmedia.ui.components.ProfileHeaderComposeParams
import com.example.socialmedia.ui.components.TabContent
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.viewmodel.AuthViewModel
import com.example.socialmedia.ui.viewmodel.ProfileViewModel
import com.example.socialmedia.utils.VerticalSpacer

data class TabContentModel(
    val imageVector: ImageVector,
    val contentDescription: String,
)

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userId: String,
) {

    val logoutState by authViewModel.logoutState.collectAsState()
    val userState by profileViewModel.userState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabList = listOf(
        TabContentModel(
            imageVector = Icons.Outlined.GridOn,
            contentDescription = "Post"
        ),
        TabContentModel(
            imageVector = Icons.Outlined.MovieCreation,
            contentDescription = "Reels"
        ),
        TabContentModel(
            imageVector = Icons.Outlined.PersonPin,
            contentDescription = "Tag"
        ),
    )

    LaunchedEffect(logoutState) {
        if (logoutState is State.Success) {
            navHostController.navigate(Screens.Login.route)
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserById(userId)
    }

    when (userState) {
        is State.Success -> {
            val user = (userState as State.Success).data
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    ProfileHeaderCompose(
                        ProfileHeaderComposeParams(
                            userName = user.username ?: "",
                            profilePicture = user.profilePicture ?: "",
                        )
                    )
                    8.VerticalSpacer()
                    Text(user.bio ?: "", style = MaterialTheme.typography.labelMedium)
                    12.VerticalSpacer()
                    Row(
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppOutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {},
                            content = {
                                Text("Edit Profile")
                            }
                        )

                        AppOutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {},
                            content = {
                                Text("Bagikan Profile")
                            }
                        )

                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.width(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Black)
                        ) {
                            Icon(
                                Icons.Outlined.PersonAdd, contentDescription = "Person Add",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    8.VerticalSpacer()
                    /// INSIGHT
                    LazyRow(modifier = Modifier.height(84.dp)) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    shape = CircleShape,
                                    border = BorderStroke(2.dp, Color.Gray),
                                    modifier = Modifier
                                        .height(64.dp)
                                        .width(64.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Outlined.Add,
                                            contentDescription = "",
                                            modifier = Modifier.size(32.dp),
                                        )
                                    }
                                }
                                2.VerticalSpacer()
                                Text("Baru")
                            }
                        }
                    }
                    /// BODY CONTENT
                    TabRow(
                        selectedTabIndex,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .padding(1.dp),
                        indicator = { tabPositions: List<TabPosition> ->
                            Box {}
                        },
                        divider = {
                            Box {}
                        },
                    ) {
                        tabList.forEachIndexed { index, tabContentModel ->

                            val hasSelect = index == selectedTabIndex

                            TabContent(
                                imageVector = tabContentModel.imageVector,
                                contentDescription = tabContentModel.contentDescription,
                                onClick = {
                                    selectedTabIndex = index
                                },
                                hasSelected = hasSelect
                            )
                        }
                    }
                }
            }
        }

        is State.Failure -> {
            val message = (userState as State.Failure).throwable.message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(message ?: "Unknown Error Occurred")
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}