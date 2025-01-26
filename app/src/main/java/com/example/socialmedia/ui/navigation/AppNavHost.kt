package com.example.socialmedia.ui.navigation

import android.os.Build
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
import com.example.socialmedia.ui.login.LoginScreen
import com.example.socialmedia.ui.main.MainScreen
import com.example.socialmedia.ui.register.RegisterScreen
import com.example.socialmedia.ui.splash.SplashScreen
import com.example.socialmedia.ui.viewmodel.AddPostViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    
    val addPostViewModel: AddPostViewModel = hiltViewModel()
    
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
            composable("create_caption?imageUri={imageUri}",
                arguments = listOf(
                    navArgument("imageUri") {
                        type = NavType.StringType
                    }
                )
            ) {
                CreateCaptionScreen(navController)
            }
        }
    }
    
    NavHost(
        navController = navController,
        graph = navGraph
    )
}