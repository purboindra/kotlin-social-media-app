package com.example.socialmedia.data.model

sealed class SavePostResult {
    object Success : SavePostResult()
    data class Error(val message: String) : SavePostResult()
}