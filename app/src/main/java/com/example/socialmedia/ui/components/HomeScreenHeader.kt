package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.socialmedia.R
import com.example.socialmedia.utils.HorizontalSpacer

@Composable
fun HomeScreenHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
    ) {
        SvgImage(
            svgResource = R.raw.instagram_horizontal_logo,
            contentDescription = "Instagram Logo",
            modifier = Modifier.height(48.dp)
        )
        Row {
            Icon(
                Icons.Outlined.AddBox,
                contentDescription = "Add Post",
                modifier = Modifier.size(32.dp)
            )
            8.HorizontalSpacer()
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(32.dp)
            
            )
            8.HorizontalSpacer()
            Icon(
                Icons.AutoMirrored.Outlined.Message,
                contentDescription = "Direct Message",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}