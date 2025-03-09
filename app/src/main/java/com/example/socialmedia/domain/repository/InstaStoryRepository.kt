package com.example.socialmedia.domain.repository

import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import kotlinx.coroutines.flow.Flow

interface InstaStoryRepository {
    suspend fun createInstaStory(video: UploadImageModel): Flow<State<ResponseModel<Boolean>>>
    suspend fun fetchAllInstastories(): Flow<State<List<InstaStory>>>
}
