package com.example.socialmedia.data.datasource_impl

import UserDatasource
import android.util.Log
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreateFollowModel
import com.example.socialmedia.data.model.FollowsUserModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Follow(
    @SerialName("id") val id: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("follower_id") val followerId: String,
    @SerialName("followed_id") val followedId: String
)

class UserDatasourceImpl(
    private val supabase: SupabaseClient,
    private val dataStore: AppDataStore,
) : UserDatasource {
    override suspend fun fetchAllUsers(query: String?): ResponseModel<List<UserModel>> {
        return try {
            
            val baseQuery = supabase.from("users")
            
            val result = if (query != null) {
                baseQuery.select {
                    filter {
                        textSearch("username_email", query, TextSearchType.NONE)
                    }
                }
            } else {
                baseQuery.select()
            }
            
            val data = result.data
            
            val users = Json.decodeFromString<List<UserModel>>(data)
            
            ResponseModel.Success(users)
        } catch (e: Throwable) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun fetchUserById(userId: String): ResponseModel<UserModel> {
        return try {
            
            val result = supabase.from("users").select(
                columns = Columns.ALL
            ) {
                filter {
                    eq("id", userId)
                }
            }
            
            val users = Json.decodeFromString<List<UserModel>>(result.data)
            
            val user = users.firstOrNull() ?: throw Exception("User not found")
            
            val currentUserId = dataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            
            val followersResult = supabase.from("follows").select(Columns.ALL) {
                filter {
                    eq("followed_id", userId)
                }
            }
            
            val followers =
                Json.decodeFromString<List<Follow>>(followersResult.data)
            
            val followingResult = supabase.from("follows").select(Columns.ALL) {
                filter {
                    eq("follower_id", userId)
                }
            }
            
            val following =
                Json.decodeFromString<List<Follow>>(followingResult.data)
            
            val followingIds = following.map { it.followedId }
            val followersIds = followers.map { it.followerId }
            
            val count = supabase.from("posts")
                .select {
                    filter {
                        eq("user", userId)
                    }
                    count(Count.EXACT)
                }.countOrNull()
            
            val userPostIds = List((count ?: 0).toInt()) { index ->
                "${userId}_$index"
            }
            
            val resultFollow = user.copy(
                followers = followersIds,
                following = followingIds,
                isFollow = followersIds.contains(currentUserId),
                posts = userPostIds
            )
            
            ResponseModel.Success(resultFollow)
            
        } catch (e: Throwable) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun followUser(userId: String): ResponseModel<Boolean> {
        return try {
            
            val currentUserId = dataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")
            
            val createFollowModel = CreateFollowModel(
                followedId = userId, followerId = currentUserId
            )
            
            supabase.from("follows").insert(createFollowModel)
            
            ResponseModel.Success(true)
            
        } catch (e: Throwable) {
            Log.e("UserDataSourceImpl", "Error Follow User: ${e.message}")
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun unFollowUser(userId: String): ResponseModel<Boolean> {
        return try {
            
            supabase.from("follows").delete {
                filter {
                    eq("followed_id", userId)
                }
            }
            
            ResponseModel.Success(true)
            
        } catch (e: Throwable) {
            Log.e("UserDataSourceImpl", "Error Un Follow User: ${e.message}")
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun fetchUserFollowing(
        userId: String,
        query: String?
    ): ResponseModel<List<FollowsUserModel>> {
        return try {
            val baseQuery = supabase.from("follows")
            val rawColumns = Columns.raw(
                """
                        id,
                        created_at,
                        followed_id (
                        id,
                        username,
                        email,
                        profile_picture,
                        created_at
                        ),
                        follower_id(
                        id,
                        username,
                        email,
                       profile_picture,
                        created_at
                        )
                    """.trimIndent()
            )
            
            val result = baseQuery.select(
                columns = rawColumns
            ) {
                filter {
                    eq("follower_id", userId)
                }
            }
            
            val following =
                Json.decodeFromString<List<FollowsUserModel>>(result.data)
            
            ResponseModel.Success(following)
            
        } catch (e: Throwable) {
            Log.e(
                "UserDataSourceImpl",
                "Error Fetch User Following: ${e.message}"
            )
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
    
    override suspend fun fetchUserFollowers(
        userId: String,
        query: String?
    ): ResponseModel<List<FollowsUserModel>> {
        return try {
            val baseQuery = supabase.from("follows")
            val rawColumns = Columns.raw(
                """
                        id,
                        created_at,
                        followed_id (
                        id,
                        username,
                        email,
                        profile_picture,
                        created_at
                        ),
                        follower_id(
                        id,
                        username,
                        email,
                       profile_picture,
                        created_at
                        )
                    """.trimIndent()
            )
            
            val result =
                if (!query.isNullOrBlank()) baseQuery.select(columns = rawColumns) {
                    filter {
                        eq("followed_id", userId)
                        textSearch("username_email", query, TextSearchType.NONE)
                    }
                } else baseQuery.select(
                    columns = rawColumns
                ) {
                    filter {
                        eq("followed_id", userId)
                    }
                }
            
            val following =
                Json.decodeFromString<List<FollowsUserModel>>(result.data)
            
            ResponseModel.Success(following)
            
        } catch (e: Throwable) {
            Log.e(
                "UserDataSourceImpl",
                "Error Fetch User Followers: ${e.message}"
            )
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
}