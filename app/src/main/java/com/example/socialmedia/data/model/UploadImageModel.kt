package com.example.socialmedia.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadImageModel(
    @SerialName("id")
    val id: String,
    @SerialName("path")
    val path: String,
    @SerialName("key")
    val key: String
)