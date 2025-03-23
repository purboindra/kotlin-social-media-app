package com.example.socialmedia.domain.supabase

import com.example.socialmedia.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
object SupabaseClientModule {
    
    private const val SUPABASE_URL = BuildConfig.PROJECT_URL
    private const val SUPABASE_KEY = BuildConfig.PROJECT_API_KEY
    const val SUPABASE_SERVER_CLIENT_ID = BuildConfig.SERVER_CLIENT_ID
    
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json { prettyPrint = true; isLenient = true })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime) {
            reconnectDelay = 5.seconds
        }
        install(Storage) {
            transferTimeout = 90.seconds
        }
        defaultSerializer = KotlinXSerializer(Json {
            prettyPrint = true
        })
    }
}
