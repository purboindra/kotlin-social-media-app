package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.datasource_impl.InstaStory
import com.example.socialmedia.data.model.InstaStoryModel
import com.example.socialmedia.data.model.PostModel
import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UploadImageModel

interface InstaStoryDatasource {
    suspend fun createInstaStory(
        video: UploadImageModel
    ): ResponseModel<Boolean>
    
    suspend fun fetchAllInstaStories(): ResponseModel<List<InstaStory>>
}