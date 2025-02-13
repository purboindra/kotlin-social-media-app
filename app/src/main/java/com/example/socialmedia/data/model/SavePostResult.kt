package com.example.socialmedia.data.model

sealed class SavePostResult {
    data object Success : SavePostResult()
    data class Error(val message: String) : SavePostResult()
}