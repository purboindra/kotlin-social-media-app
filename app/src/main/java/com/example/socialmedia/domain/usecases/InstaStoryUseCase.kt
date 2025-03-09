package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.InstaStoryRepository
import kotlinx.coroutines.flow.Flow

class InstaStoryUseCase(private val instaStoryRepository: InstaStoryRepository) {
    suspend fun createInstaStory(video: UploadImageModel): Flow<State<ResponseModel<Boolean>>> {
        return instaStoryRepository.createInstaStory(video)
    }
    
    suspend fun fetchAllInstastories(): Flow<State<List<InstaStory>>> {
        return instaStoryRepository.fetchAllInstastories()
    }
}