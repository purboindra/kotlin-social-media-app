package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FileRepositoryImpl(private val fileDatasource: FileDatasource) :
    FileRepository {
    override suspend fun uploadImage(
        imageByte: ByteArray,
        bucketId: String,
    ): Flow<State<UploadImageModel?>> = flow {
        emit(State.Loading)
        try {
            val result = fileDatasource.uploadImage(imageByte, bucketId)
            result.onSuccess {
                emit(State.Success(it))
            }.onFailure {
                emit(State.Failure(it))
            }
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
    
    override suspend fun uploadVideo(videoByte: ByteArray): Flow<State<UploadImageModel?>> =
        flow {
            emit(State.Loading)
            try {
                
                val result = fileDatasource.uploadVideo(videoByte)
                result.onSuccess {
                    emit(State.Success(it))
                }.onFailure {
                    emit(State.Failure(it))
                }
            } catch (e: Exception) {
                emit(State.Failure(e))
            }
        }
}