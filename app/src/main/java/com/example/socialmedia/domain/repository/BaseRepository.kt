package com.example.socialmedia.domain.repository

import android.util.Log
import com.example.socialmedia.data.model.State
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json


abstract class BaseRepository {
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    suspend fun fetchHttpResponse(urlString: String): HttpResponse {
        try {
            val response = client.get(urlString) {
                headers {
                    append("Content-type", "application/json")
                }
            }
            
            val debugResponse = mapOf(
                "status_code" to response.status,
                "body" to response.body(),
                "body_text" to response.bodyAsText(),
                "response" to response,
            )
            
            Log.d("fetchHttpResponse", "debugResponse: $debugResponse")
            
            return response
        } catch (e: Exception) {
            println("error: ${e.message}")
            throw e
        }
    }
    
    protected inline fun <reified T, U> (suspend () -> HttpResponse).reduce(
        crossinline block: (T) -> State<U>
    ): Flow<State<U>> {
        return flow {
            val httpResponse = invoke()
            if (httpResponse.status.isSuccess()) {
                val data = httpResponse.body<T>()
                val dataState = block.invoke(data)
                emit(dataState)
            } else {
                val throwable = Throwable(httpResponse.bodyAsText())
                val state = State.Failure(throwable)
                emit(state)
            }
        }.onStart {
            emit(State.Loading)
        }.catch {
            emit(State.Failure(it))
        }
    }
    
}