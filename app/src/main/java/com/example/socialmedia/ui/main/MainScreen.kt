package com.example.socialmedia.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.socialmedia.data.db.local.AppDataStore

@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    
    val emailFlow = remember { AppDataStore(context) }
    val email by emailFlow.email.collectAsState("")
    
    Scaffold(
        modifier = Modifier
            .safeContentPadding()
            .statusBarsPadding()
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                Text("Hello Main: $email")
            }
        }
    }
}