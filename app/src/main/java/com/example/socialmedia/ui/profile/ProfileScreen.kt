package com.example.socialmedia.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.ProfileActionButtons
import com.example.socialmedia.ui.components.ProfileHeaderCompose
import com.example.socialmedia.ui.components.ProfilePostGridCompose
import com.example.socialmedia.ui.components.ProfileSavedPostsCompose
import com.example.socialmedia.ui.components.ProfileTaggedPostsCompose
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
    val currentUserId by profileViewModel.userId.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val userState by profileViewModel.userState.collectAsState()
    val followState by profileViewModel.followState.collectAsState()
    var isLoaded by rememberSaveable { mutableStateOf(false) }
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val backstackEntry = navHostController.currentBackStackEntryAsState().value
    
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
        if (!isLoaded) {
            profileViewModel.fetchUserById(userId)
            profileViewModel.fetchPostsById(userId)
            isLoaded = true
        }
    }
    
    LaunchedEffect(backstackEntry) {
        backstackEntry?.savedStateHandle?.get<Boolean>("profileUpdated")
            ?.let { updated ->
                if (updated) {
                    profileViewModel.fetchUserById(userId)
                    backstackEntry.savedStateHandle.remove<Boolean>("profileUpdated")
                }
            }
    }
    
    Scaffold { paddingValues ->
        when (userState) {
            is State.Success -> {
                val user = (userState as State.Success).data
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .then(
                            if (userId != currentUserId) Modifier.padding(
                                paddingValues
                            ) else Modifier
                        )
                        .fillMaxSize()
                ) {
                    item {
                        ProfileHeaderCompose(
                            userModel = user,
                            navHostController
                        )
                        8.VerticalSpacer()
                        Text(
                            user.bio ?: "",
                            style = MaterialTheme.typography.labelMedium
                        )
                        12.VerticalSpacer()
                        ProfileActionButtons(
                            isCurrentUser = userId == currentUserId,
                            isFollow = user.isFollow,
                            onFollow = {
                                profileViewModel.invokeFollow(userId)
                            },
                            enabled = followState !is State.Loading,
                            userId = userId,
                            navHostController = navHostController,
                            onEdit = {
                                navHostController.navigate("edit_profile?userId=$userId")
                            }
                        )
                        8.VerticalSpacer()
                        /// INSIGHT
                        if (currentUserId == userId) LazyRow(
                            modifier = Modifier.height(
                                84.dp
                            )
                        ) {
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
                        /// TAB PROFILE
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
                        /// BODY CONTENT
                        when (selectedTabIndex) {
                            0 -> {
                                ProfilePostGridCompose(
                                    profileViewModel
                                )
                            }
                            
                            1 -> {
                                ProfileSavedPostsCompose(
                                    profileViewModel, userId
                                )
                            }
                            
                            else -> {
                                ProfileTaggedPostsCompose()
                            }
                        }
                    }
                    
                }
            }
            
            is State.Failure -> {
                val message = (userState as State.Failure).throwable.message
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