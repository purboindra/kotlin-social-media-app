package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.PostModel
import kotlin.math.ceil

@Composable
fun PostGridCompose(
    posts: List<PostModel>
) {
    val cardHeight = 96
    val items = List(10) { index -> "Item $index" }
    val rows = ceil(items.size / 3.0).toInt()
    val totalHeight = (rows * cardHeight)
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight.dp)
    ) {
        itemsIndexed(posts) { index, item ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .padding(vertical = 4.dp)
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.caption,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
