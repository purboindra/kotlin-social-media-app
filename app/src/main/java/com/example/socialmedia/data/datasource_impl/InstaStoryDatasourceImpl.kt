package com.example.socialmedia.data.datasource_impl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.socialmedia.data.datasource.InstaStoryDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreateInstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.time.Duration.Companion.hours

class InstaStoryDatasourceImpl(
    private val supabase: SupabaseClient,
    private val datastore: AppDataStore
) : InstaStoryDatasource {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createInstaStory(video: UploadImageModel): ResponseModel {
        return try {
            
            Log.d("InstaStoryDatasourceImpl", "Creating story")
            Log.d("InstaStoryDatasourceImpl", "Video: ${video.key} // ${video.path}")
            
            val userId = datastore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val uuid = UUID.randomUUID().toString()
            
            val expiresAt = Clock.System.now().plus(24.hours).toString()
            
            val createInstaStoryModel = CreateInstaStoryModel(
                id = uuid,
                videoPath = video.key,
                userId = userId,
                expiresAt = expiresAt,
                status = true
            )
            
            supabase.from("instastories").insert(createInstaStoryModel)
            
            ResponseModel.Success
            
        } catch (e: Throwable) {
            Log.e("InstaStoryDatasourceImpl", "Error creating story", e)
            ResponseModel.Error(e.message ?: "Error creating story")
        }
    }
    
}