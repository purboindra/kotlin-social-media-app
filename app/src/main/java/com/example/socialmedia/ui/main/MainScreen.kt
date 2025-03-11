package com.example.socialmedia.ui.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    val bottomNavController = rememberNavController()
    val currentDestination =
        bottomNavController.currentBackStackEntryAsState().value?.destination?.route

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
                selectedItem = currentDestination ?: Screens.Home.route,
                onSelectedItem = { route ->
                    if (route != currentDestination) {
                        bottomNavController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(bottomNavController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screens.Home.route) { HomeScreen(navHostController) }
            composable(Screens.Search.route) { SearchScreen(navHostController) }
            composable(Screens.AddPost.route) { AddPostScreen(navHostController = navHostController) }
            composable(Screens.Reels.route) { ReelsScreen(navHostController) }
            composable(Screens.Profile.route) { ProfileScreen(navHostController) }
        }
    }

}