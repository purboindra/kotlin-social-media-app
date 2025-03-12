package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.socialmedia.utils.HorizontalSpacer

data class ProfileHeaderComposeParams(
    val userName: String,
    val profilePicture: String,
)

@Composable
fun ProfileHeaderCompose(
    params: ProfileHeaderComposeParams
) {
    Row(
        modifier = Modifier
            .height(104.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.LightGray.copy(0.5f)),
            contentAlignment = Alignment.Center
        ) {
            if (params.profilePicture.isBlank()) Icon(
                Icons.Outlined.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(48.dp),
                tint = Color.Gray,
            ) else AsyncImage(
                model = params.profilePicture,
                contentDescription = params.userName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        15.HorizontalSpacer()
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileStatus(
                "Posts",
                value = "54"
            )
            ProfileStatus(
                "Followers",
                value = "923"
            )
            ProfileStatus(
                "Following",
                value = "411"
            )
        }
    }
}

@Composable
private fun ProfileStatus(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value, style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(label, style = MaterialTheme.typography.titleSmall)
    }
}