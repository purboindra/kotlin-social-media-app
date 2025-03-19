package com.example.socialmedia.domain.supabase

import com.example.socialmedia.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

object SupabaseClientModule {
    
    // S3 ACCESS KEY 5b8a198d6e74c008e71db7394a0e243f
    // S3 SECRET KEY a7b7c4b129b20af8727d2d22e9591b6aebc273db78dc23d1540c14564155f60d
    
    private const val SUPABASE_URL = BuildConfig.PROJECT_URL
    private const val SUPABASE_KEY = BuildConfig.PROJECT_API_KEY
    const val SUPABASE_SERVER_CLIENT_ID = BuildConfig.SERVER_CLIENT_ID
    
    val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        install(Storage) {
            transferTimeout = 90.seconds
        }
        defaultSerializer = KotlinXSerializer(Json {
            prettyPrint = true
        })
    }
}