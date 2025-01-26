package com.example.socialmedia.domain.repository_impl

import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FileRepositoryImpl(private val fileDatasource: FileDatasource) :
    FileRepository {
    override suspend fun uploadImage(
        imageByte: ByteArray,
    ): Flow<State<String?>> = flow {
        emit(State.Loading)
        try {
            val result = fileDatasource.uploadImage(imageByte)
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