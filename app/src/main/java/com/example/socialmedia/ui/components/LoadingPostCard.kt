package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingPostCard() {
    Column(
        modifier = Modifier
            .shimmer()
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(48.dp)
        ) {
            Box(
                modifier = Modifier.
                    height(48.dp).width(48.dp)
                    .clip(
                        RoundedCornerShape(100)
                    )
                    .background(Color.LightGray),
            )
            16.HorizontalSpacer()
            Box(
                modifier = Modifier
                    .fillMaxWidth().fillMaxHeight()
                    .background(Color.LightGray),
            )
        }
        16.VerticalSpacer()
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(Color.LightGray),
        )
    }
}