package com.example.socialmedia.ui.navigation

sealed class Screens(val route: String) {
    data object Splash : Screens("/splash")
    data object Login : Screens("/login")
    data object Register : Screens("/register")
    data object Main : Screens("/main")
    data object Home : Screens("/home")
    data object Search : Screens("/search")
    data object Reels : Screens("/reels")
    data object Profile : Screens("/profile")
    data object AddPost : Screens("/add-post")
    data object CreateCaption : Screens("/create-caption")
    data object CameraPreview : Screens("/camera-preview")
    data object StoryVideoScreen : Screens("/story-video-screen")
    data object InstaStoryScreen : Screens("/insta-story-screen")
    data object InstaStoryPreviewScreen : Screens("/insta-story-preview-screen")
    data object LikeScreen : Screens("/like-screen")
    data object Messages : Screens("/messages-screen")
}