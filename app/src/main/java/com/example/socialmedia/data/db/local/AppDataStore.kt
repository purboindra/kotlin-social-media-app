package com.example.socialmedia.data.db.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppDataStore(private val context: Context) {
    
    companion object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_IMAGE = stringPreferencesKey("user_image")
        val USER_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val USER_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_EMAIL = stringPreferencesKey("email")
    }
    
    private suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    
    private fun <T> getData(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }
    
    suspend fun saveUserEmail(email: String) {
        saveData(USER_EMAIL, email)
    }
    
    val email: Flow<String?> = getData(USER_EMAIL)
    
    suspend fun saveUserName(username: String) {
        saveData(USER_NAME, username)
    }
    
    val username: Flow<String?> = getData(USER_NAME)
    
    suspend fun saveAccessToken(accessToken: String) {
        saveData(USER_ACCESS_TOKEN, accessToken)
    }
    
    val accessToken: Flow<String?> = getData(USER_ACCESS_TOKEN)
    
    suspend fun saveRefreshToken(refreshToken: String) {
        saveData(USER_ACCESS_TOKEN, refreshToken)
    }
    
    val refreshToken: Flow<String?> = getData(USER_REFRESH_TOKEN)
    
    suspend fun saveUserId(userId: String) {
        saveData(USER_ID, userId)
    }
    
    val userId: Flow<String?> = getData(USER_ID)
}