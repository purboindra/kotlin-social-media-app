package com.example.socialmedia.data.datasource_impl

import android.util.Log
import com.example.socialmedia.data.datasource.MessageDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.MessageModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SendMessageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

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
            
            val existingChat = supabaseClient.from("messages").select(
                columns = Columns.ALL
            ) {
                filter {
                    eq("sender_id", currentUserId)
                    and {
                        eq("receiver_id", receiverId)
                    }
                }
            }
            
            val chats =
                Json.decodeFromString<List<MessageModel>>(existingChat.data)
            
            if (chats.isNotEmpty()) {
                val chatId = chats.first().id
                val sendMessageModel = SendMessageModel(
                    senderId = currentUserId,
                    receiverId = receiverId,
                    message = message,
                    chatId = chatId
                )
                supabaseClient.from("messages").insert(sendMessageModel)
                return ResponseModel.Success(true)
            }
            
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
        val channel = supabaseClient.channel("messages")
        val broadcastFlow =
            channel.broadcastFlow<SendMessageModel>("messages")
        
        return broadcastFlow
            .onEach { message ->
                onMessageReceived(message)
            }.catch { e ->
                Log.e("MessageDatasourceImpl", "Error in broadcastFlow: $e")
            }
            .launchIn(coroutineScope)
            .also {
                channel.subscribe(blockUntilSubscribed = true)
            }
    }
    
    override suspend fun unSubscribeToMessage() {
        val channel = supabaseClient.channel("messages")
        supabaseClient.realtime.removeChannel(channel)
        val channels = supabaseClient.realtime.subscriptions.entries
        Log.d("MessageDatasourceImpl", "Channels: $channels")
    }
    
    override suspend fun fetchMessages(
    ): ResponseModel<List<MessageModel>> {
        return try {
            
            val userId = appDataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val result = supabaseClient.from("messages").select(
                columns = Columns.ALL
            ) {
                filter {
                    eq("sender_id", userId)
                }
            }
            
            val data = result.data
            
            val messages = Json.decodeFromString<List<MessageModel>>(data)
            ResponseModel.Success(messages)
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
    
    override suspend fun fetchConversation(
        receiverId: String,
        senderId: String
    ): ResponseModel<List<MessageModel>> {
        return try {
            val result = supabaseClient.from("messages").select(
                columns = Columns.ALL
            ) {
                filter {
                    eq("sender_id", senderId)
                    and {
                        eq("receiver_id", receiverId)
                    }
                }
            }
            
            val data = result.data
            
            val messages = Json.decodeFromString<List<MessageModel>>(data)
            
            ResponseModel.Success(messages)
            
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
    
}