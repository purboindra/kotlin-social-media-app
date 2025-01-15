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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.socialmedia.R
import com.example.socialmedia.ui.components.SvgImage

@Composable
fun SplashScreen() {
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
                Text("Instagram", style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ))
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
