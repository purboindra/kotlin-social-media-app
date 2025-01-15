package com.example.socialmedia.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen() {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues), contentAlignment = Alignment.Center) {
            Text("Social Media App")
        }
    }
}