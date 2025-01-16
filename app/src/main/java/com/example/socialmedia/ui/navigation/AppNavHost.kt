package com.example.socialmedia.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.socialmedia.ui.login.LoginScreen
import com.example.socialmedia.ui.splash.SplashScreen

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
        }
    }
    
    NavHost(
        navController = navController,
        graph = navGraph
    )
}