package com.example.socialmedia.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileActionButtons(
    isCurrentUser: Boolean,
    isFollow: Boolean,
    onFollow: () -> Unit,
    onEdit:()->Unit,
    enabled: Boolean,
    userId: String,
    navHostController: NavHostController
) {
    
    if (!isCurrentUser) {
        AppElevatedButton(
            enabled = enabled,
            onClick = onFollow,
            text = if (isFollow) "Unfollow" else "Follow",
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(6.dp)
        )
        return
    }
    Row(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = {
               onEdit()
            },
            content = {
                Text("Edit Profile")
            }
        )
        
        AppOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = {},
            content = {
                Text("Bagikan Profile")
            }
        )
        
        OutlinedButton(
            onClick = {},
            modifier = Modifier.width(56.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Icon(
                Icons.Outlined.PersonAdd,
                contentDescription = "Person Add",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}