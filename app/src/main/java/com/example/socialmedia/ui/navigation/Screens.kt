package com.example.socialmedia.ui.navigation

sealed class Screens(val route: String) {
    data object Splash : Screens("/splash")
    data object Login : Screens("/login")
    data object Register : Screens("/register")
    data object Main : Screens("/main")
}