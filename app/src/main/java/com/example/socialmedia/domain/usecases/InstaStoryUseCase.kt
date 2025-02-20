package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.InstaStoryRepository
import kotlinx.coroutines.flow.Flow

class InstaStoryUseCase(private val instaStoryRepository: InstaStoryRepository) {
    suspend fun createInstaStory(video: UploadImageModel): Flow<State<ResponseModel>> {
        return instaStoryRepository.createInstaStory(video)
    }
}