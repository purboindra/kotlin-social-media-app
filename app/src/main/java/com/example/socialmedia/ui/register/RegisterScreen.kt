package com.example.socialmedia.ui.register

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun RegisterScreen(navHostController: NavHostController) {

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .safeContentPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                SvgImage(
                    svgResource = R.raw.instagram_logo,
                    contentDescription = "Instagram Logo",
                    modifier = Modifier.size(48.dp)
                )
                10.VerticalSpacer()
                AppOutlinedTextField(
                    query = "",
                    onValueChange = {},
                    placeholderText = "Email"
                )
                5.VerticalSpacer()
                AppOutlinedTextField(
                    query = "",
                    onValueChange = {},
                    placeholderText = "Username"
                )
                5.VerticalSpacer()
                AppOutlinedTextField(
                    query = "",
                    onValueChange = {},
                    placeholderText = "Password"
                )
                5.VerticalSpacer()
                AppOutlinedTextField(
                    query = "",
                    onValueChange = {},
                    placeholderText = "Confirm Password"
                )
            }

        }
    }

}