package com.example.socialmedia.data.datasource_impl

import UserDatasource
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.serialization.json.Json

class UserDatasourceImpl(
    private val supabase: SupabaseClient
) : UserDatasource {
    override suspend fun search(query: String): ResponseModel<List<UserModel>> {
        return try {

            val result = supabase.from("users").select {
                filter {
                    textSearch(
                        "username_email", query, TextSearchType.NONE
                    )
                }
            }

            val data = result.data

            val users = Json.decodeFromString<List<UserModel>>(data)

            ResponseModel.Success(users)
        } catch (e: Throwable) {
            ResponseModel.Error(e.message ?: "Something went wrong...")
        }
    }
}