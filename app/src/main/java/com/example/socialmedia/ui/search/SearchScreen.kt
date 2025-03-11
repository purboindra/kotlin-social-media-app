package com.example.socialmedia.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun SearchScreen(navHostController: NavHostController) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            /// SEARCH
            AppOutlinedTextField(
                query = "",
                onValueChange = {},
                placeholderText = "Search...",
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    )
            )
            5.VerticalSpacer()
            /// CONTENT
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                items(10) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height((100..300).random().dp)
                        .background(Color.LightGray))
                }

            }
        }
    }
}