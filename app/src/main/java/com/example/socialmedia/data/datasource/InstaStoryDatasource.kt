package com.example.socialmedia.data.datasource

import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UploadImageModel

interface InstaStoryDatasource {
    suspend fun createInstaStory(
        video: UploadImageModel
    ): ResponseModel
}