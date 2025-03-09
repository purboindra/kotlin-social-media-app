package com.example.socialmedia.data.model

sealed class ResponseModel<out T> {
    data class Success<out T>(val value: T) : ResponseModel<T>()
    data class Error(val message: String) : ResponseModel<Nothing>()
}