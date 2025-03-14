package com.example.socialmedia.data.datasource_impl

import UserDatasource
import android.util.Log
import androidx.datastore.dataStore
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.CreateFollowModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Follow(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("follower_id")
    val followerId: String,
    @SerialName("followed_id")
    val followedId: String
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

            val data = result.data

            val users = Json.decodeFromString<List<UserModel>>(data)

            val user = users.firstOrNull() ?: throw Exception("User not found")

            val currentUserId = dataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")

            val isFollowingResult =
                supabase.from("follows").select(Columns.ALL) {
                    filter {
                        eq("follower_id", currentUserId)
                        eq("followed_id", userId)
                    }
                }

            val isFollowing =
                Json.decodeFromString<List<Follow>>(isFollowingResult.data)

            val userFollow = user.copy(
                isFollow = isFollowing.isNotEmpty()
            )

            val followersResult = supabase.from("follows").select(Columns.ALL, {
                filter {
                    eq("followed_id", currentUserId)
                }
            })

            val followers = Json.decodeFromString<List<Follow>>(followersResult.data)

            val followingResult = supabase.from("follows").select(Columns.ALL, {
                filter {
                    eq("follower_id", currentUserId)
                }
            })

            val following = Json.decodeFromString<List<Follow>>(followingResult.data)

            val followingIds = following.map { it.followedId }
            val followersIds = followers.map { it.followerId }

            val resultFollow = userFollow.copy(
                followers = followersIds,
                following = followingIds
            )

            val postsResult = supabase.from("posts").select(Columns.ALL, {
                filter {
                    eq("user", currentUserId)
                }
            })

            val post = Json.decodeFromString<List<PostModel>>(postsResult.data)
            val userPostIds = post.map { it.user.id }

            val resultPost = resultFollow.copy(
                posts = userPostIds
            )

            ResponseModel.Success(resultPost)

        } catch (e: Throwable) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }

    override suspend fun followUser(userId: String): ResponseModel<Boolean> {
        return try {

            val currentUserId = dataStore.userId.firstOrNull()
                ?: throw Exception("User ID not found")

            val createFollowModel = CreateFollowModel(
                followedId = userId,
                followerId = currentUserId
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
}