package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFollowModel(
    @SerialName("followed_id")
    val followedId: String,
    @SerialName("follower_id")
    val followerId: String,
)