package com.example.socialmedia.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.ui.add_post.AddPostScreen
import com.example.socialmedia.ui.components.AppBottomNavigationBar
import com.example.socialmedia.ui.components.BottomNavigationItem
import com.example.socialmedia.ui.home.HomeScreen
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.profile.ProfileScreen
import com.example.socialmedia.ui.reels.ReelsScreen
import com.example.socialmedia.ui.search.SearchScreen
import com.example.socialmedia.ui.viewmodel.MainViewModel


@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    
    val context = LocalContext.current
    
    val emailFlow = remember { AppDataStore(context) }
    val email by emailFlow.email.collectAsState("")
    val bottomNavbarIndex by mainViewModel.bottomNavbarIndex.collectAsState()
    
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedItem = Icons.Filled.Home,
            unSelectedItem = Icons.Outlined.Home,
            hasNews = false,
            route = Screens.Home.route
        ),
        
        BottomNavigationItem(
            title = "Search",
            selectedItem = Icons.Filled.Search,
            unSelectedItem = Icons.Outlined.Search,
            hasNews = false,
            route = Screens.Search.route
        ),
        BottomNavigationItem(
            title = "Add Post",
            selectedItem = Icons.Outlined.AddBox,
            unSelectedItem = Icons.Outlined.AddBox,
            hasNews = false,
            route = Screens.AddPost.route
        ),
        BottomNavigationItem(
            title = "Reels",
            selectedItem = Icons.Filled.MovieCreation,
            unSelectedItem = Icons.Outlined.MovieCreation,
            hasNews = false,
            route = Screens.Reels.route
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedItem = Icons.Filled.Person,
            unSelectedItem = Icons.Outlined.Person,
            hasNews = false,
            route = Screens.Profile.route
        ),
    )
    
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                items = items,
                selectedItem = bottomNavbarIndex,
                onSelectedItem = { index ->
                    mainViewModel.onSelectedBottomNavbar(index)
                    navHostController.navigate(
                        items[index].route
                    ) {
                        popUpTo(Screens.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MainNavHost(navController = navHostController)
        }
    }
}