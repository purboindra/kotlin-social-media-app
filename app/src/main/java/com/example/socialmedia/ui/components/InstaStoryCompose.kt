package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.utils.VerticalSpacer

val dummyUsers = listOf(
    mapOf(
        "username" to "Ilya",
        "imageUrl" to "https://images.unsplash.com/photo-1512316609839-ce289d3eba0a?q=80&w=1738&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    mapOf(
        "username" to "Olga",
        "imageUrl" to "https://images.unsplash.com/photo-1614786269829-d24616faf56d?q=80&w=1635&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    mapOf(
        "username" to "Daenarys",
        "imageUrl" to "https://images.unsplash.com/photo-1533973860717-d49dfd14cf64?q=80&w=1571&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    mapOf(
        "username" to "Arya Targaryen",
        "imageUrl" to "https://images.unsplash.com/photo-1525299374597-911581e1bdef?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    
    mapOf(
        "username" to "Jenny",
        "imageUrl" to "https://images.unsplash.com/photo-1510706019500-d23a509eecd4?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    mapOf(
        "username" to "Cersei",
        "imageUrl" to "https://images.unsplash.com/photo-1577912931989-cf038df56a56?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
)

@Composable
fun InstaStoryCompose() {
    
    val colorStops = arrayOf(
        0.0f to Color.Yellow,
        0.5f to Color.Red,
        1f to Color.Red,
    )
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .padding(horizontal = 8.dp)
    ) {
        items(dummyUsers) { user ->
            
            Column(
                modifier = Modifier
                    .fillParentMaxHeight()
                    .width(92.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(shape = RoundedCornerShape(100))
                            .background(
                                Brush.horizontalGradient(colorStops = colorStops)
                            )
                    )
                    
                    
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(shape = RoundedCornerShape(100))
                            .background(Color.White)
                            .align(Alignment.Center)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(shape = RoundedCornerShape(100))
                            .background(GrayPrimary)
                            .align(Alignment.Center)
                    ) {
                        AsyncImage(
                            model = user["imageUrl"] as String,
                            contentDescription = user["username"] as String,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(shape = RoundedCornerShape(100))
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(shape = RoundedCornerShape(100))
                                .background(Color.White)
                                .align(Alignment.BottomEnd)
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .offset(x = 7.dp, y = 7.dp)
                                .clip(shape = RoundedCornerShape(100))
                                .background(BlueLight)
                        ) {
                            Icon(
                                Icons.Outlined.Add, contentDescription = "Add",
                                modifier = Modifier
                                    .size(16.dp)
                                    .offset(
                                        x = 3.dp, y = 3.dp
                                    ),
                                tint = Color.White,
                            )
                        }
                    }
                    
                }
                
                5.VerticalSpacer()
                Text(
                    user["username"] as String,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 13.sp,
                    )
                )
            }
        }
    }
}