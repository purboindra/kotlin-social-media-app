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
    
    val supabaseUrl = BuildConfig.PROJECT_URL
    val supabaseKey = BuildConfig.PROJECT_API_KEY
    
    val supabase = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey,
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