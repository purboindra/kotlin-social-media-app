package com.example.socialmedia.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    
    val context = LocalContext.current
    val appDataStore = remember { AppDataStore(context) }
    val accessToken by appDataStore.accessToken.collectAsState(null)
    
    LaunchedEffect(Unit) {
        delay(1000)
        if (accessToken == null) {
            navHostController.navigate(Screens.Main.route)
        } else {
            navHostController.navigate(Screens.Main.route)
        }
    }
    
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Instagram",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                SvgImage(
                    R.raw.instagram_logo,
                    contentDescription = "Instagram Logo",
                    modifier = Modifier.size(102.dp)
                )
            }
        }
    }
}
