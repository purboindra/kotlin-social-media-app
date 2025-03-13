package com.example.socialmedia.data.datasource_impl

import UserDatasource
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.serialization.json.Json

class UserDatasourceImpl(
    private val supabase: SupabaseClient
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
                columns = Columns.ALL, {
                    filter {
                        eq("id", userId)
                    }
                }
            )

            val data = result.data

            val users = Json.decodeFromString<List<UserModel>>(data)

            val user = users.firstOrNull() ?: throw Exception("User not found")

            ResponseModel.Success(user)

        } catch (e: Throwable) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
}