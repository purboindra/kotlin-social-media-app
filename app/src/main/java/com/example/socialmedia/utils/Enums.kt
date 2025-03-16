package com.example.socialmedia.utils

enum class ConnectionType {
    FOLLOWING, FOLLOWERS;
    
    companion object {
        fun fromString(value: String): ConnectionType? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}