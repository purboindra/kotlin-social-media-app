package com.example.socialmedia.data.model

sealed class ResponseModel {
    data object Success : ResponseModel()
    data class Error(val message: String) : ResponseModel()
}