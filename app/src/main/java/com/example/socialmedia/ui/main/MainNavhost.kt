package com.example.socialmedia.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialmedia.ui.add_post.AddPostScreen
import com.example.socialmedia.ui.home.HomeScreen
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.profile.ProfileScreen
import com.example.socialmedia.ui.reels.ReelsScreen
import com.example.socialmedia.ui.search.SearchScreen

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) { HomeScreen(navController) }
        composable(Screens.Search.route) { SearchScreen(navController) }
        composable(Screens.AddPost.route) { AddPostScreen(navHostController = navController) }
        composable(Screens.Reels.route) { ReelsScreen(navController) }
        composable(Screens.Profile.route) { ProfileScreen(navController) }
    }
}
