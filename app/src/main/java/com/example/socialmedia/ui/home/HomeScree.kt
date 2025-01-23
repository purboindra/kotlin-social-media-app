package com.example.socialmedia.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.socialmedia.ui.components.HomeScreenHeader
import com.example.socialmedia.ui.components.InstaStoryComponent
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreenHeader()
        10.VerticalSpacer()
        LazyColumn {
            item {
                InstaStoryComponent()
            }
        }
    }
}