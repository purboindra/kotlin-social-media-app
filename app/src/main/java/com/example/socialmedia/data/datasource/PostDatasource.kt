package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.datasource_impl.FetchLikesModel
import com.example.socialmedia.data.datasource_impl.SavedPostModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.SavePostResult
import com.example.socialmedia.data.model.UploadImageModel

interface PostDatasource {
    suspend fun createPost(
        image: UploadImageModel,
        caption: String,
        taggedUsers: List<String>?,
        taggedLocation: String,
    ): Result<Boolean>
    
    suspend fun fetchAllPosts(): Result<List<PostModel>>
    suspend fun fetchPostsById(userId: String): ResponseModel<List<PostModel>>
    suspend fun createLike(id: String): Result<Boolean>
    suspend fun deleteLike(id: String): Result<Boolean>
    suspend fun fetchAllLikes(): Result<List<FetchLikesModel>>
    suspend fun createComment(id: String, comment: String): Result<Boolean>
    suspend fun fetchSavedPostsByUserId(userId: String): ResponseModel<List<SavedPostModel>>
    suspend fun savedPost(id: String): SavePostResult
    suspend fun deleteSavedPost(id: String): SavePostResult
}