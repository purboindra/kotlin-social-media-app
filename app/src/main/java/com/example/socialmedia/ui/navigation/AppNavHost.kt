package com.example.socialmedia.ui.navigation

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.example.socialmedia.ui.add_post.CreateCaptionScreen
import com.example.socialmedia.ui.camera.CameraPreviewScreen
import com.example.socialmedia.ui.home.HomeScreen
import com.example.socialmedia.ui.login.LoginScreen
import com.example.socialmedia.ui.main.MainScreen
import com.example.socialmedia.ui.profile.ProfileScreen
import com.example.socialmedia.ui.reels.ReelsScreen
import com.example.socialmedia.ui.register.RegisterScreen
import com.example.socialmedia.ui.search.SearchScreen
import com.example.socialmedia.ui.splash.SplashScreen
import com.example.socialmedia.ui.story.StoryVideoScreen
import com.example.socialmedia.ui.viewmodel.SharedFileViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val navGraph = remember(navController) {
        navController.createGraph(
            startDestination = Screens.Splash.route
        ) {
            composable(Screens.Splash.route) {
                SplashScreen(navHostController = navController)
            }
            composable(Screens.Login.route) {
                LoginScreen(navHostController = navController)
            }
            composable(Screens.Register.route) {
                RegisterScreen(navHostController = navController)
            }
            composable(Screens.Main.route) {
                MainScreen(navHostController = navController)
            }
            composable(Screens.Home.route) {
                HomeScreen(navController)
            }
            composable(Screens.Search.route) {
                SearchScreen(navController)
            }
            composable(Screens.Reels.route) {
                ReelsScreen(navController)
            }
            composable(Screens.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screens.CameraPreview.route) {
                val sharedFileViewModel: SharedFileViewModel = hiltViewModel()
                
                CameraPreviewScreen(
                    navController,
                    sharedFileViewModel = sharedFileViewModel
                )
            }
            composable(Screens.StoryVideoScreen.route) {
                StoryVideoScreen()
            }
            composable("create_caption?imageUri={imageUri}&videoUri={videoUri}",
                arguments = listOf(
                    navArgument("imageUri") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                        
                    },
                    navArgument("videoUri") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                        
                    }
                )
            ) {
                val sharedFileViewModel: SharedFileViewModel = hiltViewModel()
                BackHandler(true) {
                }
                CreateCaptionScreen(navController, sharedFileViewModel)
                
            }
        }
    }
    
    NavHost(
        navController = navController,
        graph = navGraph
    )
}