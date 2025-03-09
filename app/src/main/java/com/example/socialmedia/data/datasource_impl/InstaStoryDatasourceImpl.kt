package com.example.socialmedia.data.datasource_impl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.socialmedia.data.datasource.InstaStoryDatasource
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreateInstaStoryModel
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UploadImageModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

data class InstaStory(
    val id: String,
    val profilePicture: String,
    val fullName: String,
    val instastories: MutableList<InstaStoryModel>
)

class InstaStoryDatasourceImpl(
    private val supabase: SupabaseClient,
    private val datastore: AppDataStore
) : InstaStoryDatasource {
    
    override suspend fun fetchAllInstaStories(): ResponseModel<List<InstaStory>> {
        
        val instastories: MutableList<InstaStory> = mutableListOf()
        val groupedStories = mutableMapOf<String, InstaStory>()
        
        return try {
            
            val currentUserId = datastore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val bucket = supabase.storage.from("instastories")
            
            val stories = supabase.from("instastories").select(
                columns = Columns.raw(
                    """
                   id,
                   content_path,
                   content_url,
                   expires_at,
                   status,
                   duration,
                   created_at,
                   user(
                    id,
                    full_name,
                    profile_picture
                   )
               """.trimIndent()
                )
            )
            
            val data = stories.data
            
            val parsedData = Json.decodeFromString<List<InstaStoryModel>>(data)
            
            val updateInstaStories = parsedData.map { instastory ->
                Log.d("PostDataSourceImpl", "InstaStory: $instastory")
                val url = try {
                    bucket.createSignedUrl(
                        path = instastory.contentPath,
                        expiresIn = 5.minutes
                    )
                } catch (e: RestException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url RestException",
                        e
                    )
                    ""
                } catch (e: HttpRequestException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url HttpRequestException",
                        e
                    )
                    ""
                } catch (e: HttpRequestTimeoutException) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url HttpRequestTimeoutException",
                        e
                    )
                    throw e
                } catch (e: Exception) {
                    Log.e(
                        "PostDataSourceImpl",
                        "Error creating signed url Exception",
                        e
                    )
                    ""
                }
                
                instastory.copy(
                    contentUrl = url
                )
            }
            
            for (instastory in updateInstaStories) {
                val userId = instastory.user.id
                
                val profilePicture =
                    instastory.user.profilePicture ?: ""
                
                val fullName = instastory.user.fullName ?: ""
                
                groupedStories.putIfAbsent(
                    userId, InstaStory(
                        id = userId,
                        profilePicture = profilePicture,
                        fullName = fullName,
                        instastories = mutableListOf(),
                    )
                )
                val userGroup = groupedStories[userId]
                userGroup?.instastories?.add(instastory)
            }
            
            instastories.addAll(groupedStories.values)
            
            val findCurrentUserInstaStory = instastories.find {
                it.id == currentUserId
            }
            
            val profilePicture = datastore.profilePicture.firstOrNull()
            
            if (findCurrentUserInstaStory == null) {
                val currentUserInstaStory = InstaStory(
                    id = currentUserId,
                    profilePicture = profilePicture ?: "",
                    fullName = "",
                    instastories = mutableListOf(),
                )
                instastories.add(currentUserInstaStory)
            }
            
            val sortedInstastories = instastories.sortedWith(
                compareByDescending {
                    if (it.id == currentUserId) 1 else 0
                }
            )
            
            ResponseModel.Success(sortedInstastories)
        } catch (e: Throwable) {
            Log.e("InstaStoryDatasourceImpl", "Error fetching stories", e)
            ResponseModel.Error(e.message ?: "Error fetching stories")
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createInstaStory(video: UploadImageModel): ResponseModel<Boolean> {
        return try {
            
            Log.d("InstaStoryDatasourceImpl", "Creating story")
            Log.d(
                "InstaStoryDatasourceImpl",
                "Video: ${video.key} // ${video.path}"
            )
            
            val userId = datastore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val uuid = UUID.randomUUID().toString()
            
            val expiresAt = Clock.System.now().plus(24.hours).toString()
            
            val createInstaStoryModel = CreateInstaStoryModel(
                id = uuid,
                contentPath = video.path,
                contentUrl = "",
                user = userId,
                expiresAt = expiresAt,
                status = true,
                duration = 10,
            )
            
            supabase.from("instastories").insert(createInstaStoryModel)
            
            ResponseModel.Success(true)
            
        } catch (e: Throwable) {
            Log.e("InstaStoryDatasourceImpl", "Error creating story", e)
            ResponseModel.Error(e.message ?: "Error creating story")
        }
    }
    
}