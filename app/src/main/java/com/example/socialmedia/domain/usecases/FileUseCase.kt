package com.example.socialmedia.domain.usecases

import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UploadImageModel
import com.example.socialmedia.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow

class FileUseCase(private val fileRepository: FileRepository) {
    suspend fun uploadFile(imageByte: ByteArray): Flow<State<UploadImageModel?>> {
        return fileRepository.uploadImage(imageByte)
    }
}