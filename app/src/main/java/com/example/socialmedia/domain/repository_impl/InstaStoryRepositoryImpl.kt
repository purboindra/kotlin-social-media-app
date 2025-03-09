package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.InstaStoryDatasource
import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.InstaStoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InstaStoryRepositoryImpl(
    private val instaStoryDatasource: InstaStoryDatasource
) : InstaStoryRepository {
    override suspend fun createInstaStory(video: UploadImageModel): Flow<State<ResponseModel<Boolean>>> =
        flow {
            emit(State.Loading)
            when (val result = instaStoryDatasource.createInstaStory(video)) {
                is ResponseModel.Success -> emit(State.Success(result))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
    
    override suspend fun fetchAllInstastories(): Flow<State<List<InstaStory>>> =
        flow {
            emit(State.Loading)
            when (val result = instaStoryDatasource.fetchAllInstaStories()) {
                is ResponseModel.Success -> emit(State.Success(result.value))
                is ResponseModel.Error -> emit(State.Failure(Throwable(result.message)))
            }
        }
}