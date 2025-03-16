package com.example.socialmedia.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.example.socialmedia.ui.add_post.CreateCaptionScreen
import com.example.socialmedia.ui.camera.CameraPreviewScreen
import com.example.socialmedia.ui.follows.FollowScreen
import com.example.socialmedia.ui.home.HomeScreen
import com.example.socialmedia.ui.insta_story.InstaStoryPreviewScreen
import com.example.socialmedia.ui.insta_story.InstaStoryScreen
import com.example.socialmedia.ui.like.LikeScreen
import com.example.socialmedia.ui.login.LoginScreen
import com.example.socialmedia.ui.main.MainScreen
import com.example.socialmedia.ui.profile.ProfileScreen
import com.example.socialmedia.ui.reels.ReelsScreen
import com.example.socialmedia.ui.register.RegisterScreen
import com.example.socialmedia.ui.search.SearchScreen
import com.example.socialmedia.ui.splash.SplashScreen
import com.example.socialmedia.ui.story.StoryVideoScreen
import com.example.socialmedia.ui.viewmodel.SharedFileViewModel
import com.example.socialmedia.utils.ConnectionType

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
                MainScreen(
                    navHostController = navController
                )
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
            composable("/follows-screen?userId={userId}?type={type}",
                arguments = listOf(
                    navArgument(
                        "userId",
                    ) {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = ""
                    },
                    navArgument(
                        "type",
                    ) {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = ConnectionType.FOLLOWING.name
                    }
                )) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                val typeString = backStackEntry.arguments?.getString("type") ?: ConnectionType.FOLLOWING.name
                val type = ConnectionType.fromString(typeString) ?: ConnectionType.FOLLOWING
                
                FollowScreen(userId = userId, type = type, navController)
            }
            composable("/profile?userId={userId}", arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""
                }
            )) { backStackEntry ->
                val userId =
                    backStackEntry.arguments?.getString("userId") ?: ""
                ProfileScreen(navController, userId = userId)
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
            composable(Screens.LikeScreen.route) {
                LikeScreen(navHostController = navController)
            }
            composable("instastory_preview_screen?imageUrl={imageUrl}&profilePicture={profilePicture}",
                arguments = listOf(
                    navArgument(
                        "imageUrl"
                    ) {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = ""
                    },
                    navArgument(
                        "profilePicture"
                    ) {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = ""
                    }
                )
            ) {
                InstaStoryPreviewScreen(navController)
            }
            composable("instastory_screen?imageUrl={imageUrl}",
                arguments = listOf(
                    navArgument(
                        "imageUrl"
                    ) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                InstaStoryScreen(navController)
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
                BackHandler(true) {
                }
                CreateCaptionScreen(navController)
                
            }
        }
    }
    
    NavHost(
        navController = navController,
        graph = navGraph
    )
}