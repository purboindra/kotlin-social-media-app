package com.example.socialmedia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.utils.ConnectionType
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer


@Composable
fun ProfileHeaderCompose(
    userModel: UserModel,
    navHostController: NavHostController
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
            if (userModel.profilePicture.isNullOrBlank()) Icon(
                Icons.Outlined.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(48.dp),
                tint = Color.Gray,
            ) else AsyncImage(
                model = userModel.profilePicture,
                contentDescription = userModel.username,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        15.HorizontalSpacer()
        Column {
            Text(
                userModel.username ?: "",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                )
            )
            8.VerticalSpacer()
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileStatus(
                    "Posts",
                    value = (userModel.posts?.size ?: 0).toString()
                )
                ProfileStatus(
                    "Followers",
                    value = (userModel.followers?.size ?: 0).toString(),
                    onTap = {
                        navHostController.navigate("/follows-screen?userId=${userModel.id}&type=${ConnectionType.FOLLOWERS.name}")
                    }
                )
                ProfileStatus(
                    "Following",
                    value = (userModel.following?.size ?: 0).toString(),
                    onTap = {
                        navHostController.navigate("/follows-screen?userId=${userModel.id}&type=${ConnectionType.FOLLOWING.name}")
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileStatus(
    label: String,
    value: String,
    onTap: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onTap() }) {
        Text(
            value, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(label, style = MaterialTheme.typography.titleSmall)
    }
}