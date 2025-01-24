package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmedia.ui.theme.BlueLight
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.utils.VerticalSpacer

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
        items(25) {
            Column(
                modifier = Modifier
                    .fillParentMaxHeight()
                    .width(92.dp)
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
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(shape = RoundedCornerShape(100)).align(Alignment.BottomEnd)
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
                                .size(22.dp).offset(x = 7.dp, y = 7.dp)
                                .clip(shape = RoundedCornerShape(100))
                                .background(BlueLight)
                        ){
                            Icon(Icons.Outlined.Add, contentDescription = "Add", modifier = Modifier.size(16.dp).offset(
                                x = 3.dp, y = 3.dp
                            ), tint = Color.White,)
                        }
                    }
                    
                }
                
                5.VerticalSpacer()
                Text("Purboyndrasasasasa", style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 13.sp,
                ))
            }
        }
    }
}