package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit) = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    style: TextStyle? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = style ?: MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.W500,
                )
            )
        },
        actions = actions,
        navigationIcon = navigationIcon,
        colors = colors,
    )
}
