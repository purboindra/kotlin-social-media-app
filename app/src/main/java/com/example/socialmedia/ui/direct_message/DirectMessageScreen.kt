package com.example.socialmedia.ui.direct_message

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun DirectMessageScreen(
    userId: String,
    navHostController: NavHostController
) {
    Scaffold { paddingValues ->
        Text(
            "Direct Message Screen",
            modifier = Modifier.padding(paddingValues)
        )
    }
}