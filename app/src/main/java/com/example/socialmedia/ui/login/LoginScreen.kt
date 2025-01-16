package com.example.socialmedia.ui.login

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun LoginScreen(navHostController: NavHostController) {
    
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .safeContentPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                SvgImage(
                    svgResource = R.raw.instagram_logo,
                    contentDescription = "Instagram Logo",
                    modifier = Modifier.size(48.dp)
                )
                10.VerticalSpacer()
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillParentMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = GrayPrimary,
                    ),
                    placeholder = {
                        Text(
                            "Username or email",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = GrayPrimary,
                            )
                        )
                    },
                )
                8.VerticalSpacer()
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillParentMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = GrayPrimary,
                    ),
                    placeholder = {
                        Text(
                            "Password",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = GrayPrimary,
                            )
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Outlined.Visibility,
                            contentDescription = "Visibility",
                        )
                    }
                )
                10.VerticalSpacer()
               AppElevatedButton(
                   onClick = {},
                    text = "Log In"
               )
            }
        }
    }
    
}