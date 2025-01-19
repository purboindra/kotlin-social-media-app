package com.example.socialmedia.domain.supabase

import com.example.socialmedia.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds


object SupabaseClientModule {
    
    private const val SUPABASE_URL = BuildConfig.PROJECT_URL
    private const val SUPABASE_KEY = BuildConfig.PROJECT_API_KEY
    const val SUPABASE_SERVER_CLIENT_ID = BuildConfig.SERVER_CLIENT_ID
    
    val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage) {
            transferTimeout = 90.seconds
        }
        defaultSerializer = KotlinXSerializer(Json {
            prettyPrint = true
        })
    }
}