package com.example.socialmedia.data.datasource_impl

import com.example.socialmedia.data.datasource.MessageDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class MessageDatasourceImpl(
    private val supabaseClient: SupabaseClient,
    private val appDataStore: AppDataStore,
) : MessageDatasource {
    override suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String
    ): ResponseModel<Boolean> {
        return try {
            
            val currentUserId = appDataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val sendMessageModel = SendMessageModel(
                senderId = currentUserId,
                receiverId = receiverId,
                message = message
            )
            
            supabaseClient.from("messages").insert(sendMessageModel)
            
            ResponseModel.Success(true)
            
        } catch (e: SerializationException) {
            ResponseModel.Error(
                e.message ?: "Something went wrong while parse data..."
            )
        } catch (e: IOException) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        } catch (e: Exception) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun subscribeToMessage(
        chatId: String,
        coroutineScope: CoroutineScope,
        onMessageReceived: (SendMessageModel) -> Unit
    ): Job {
        val channel = supabaseClient.channel("messages_channel")
        val broadcastFlow =
            channel.broadcastFlow<SendMessageModel>("new_message")
        
        return broadcastFlow
            .onEach { message ->
                onMessageReceived(message)
            }
            .launchIn(coroutineScope)
            .also {
                channel.subscribe(blockUntilSubscribed = true)
            }
    }
}