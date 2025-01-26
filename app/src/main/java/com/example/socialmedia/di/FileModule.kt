package com.example.socialmedia.di

import com.example.socialmedia.data.datasource.FileDatasource
import com.example.socialmedia.data.datasource_impl.FileDataSourceImpl
import com.example.socialmedia.domain.repository_impl.FileRepositoryImpl
import com.example.socialmedia.domain.usecases.FileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    @Singleton
    fun providesFileDatasourceImpl(): FileDataSourceImpl {
        return FileDataSourceImpl(
            SupabaseModule.provideSupabaseClient(),
        )
    }
    
    @Provides
    @Singleton
    fun provideFileRepositoryImpl(): FileRepositoryImpl {
        return FileRepositoryImpl(providesFileDatasourceImpl())
    }
    
    @Provides
    @Singleton
    fun provideFileUseCase(): FileUseCase {
        return FileUseCase(provideFileRepositoryImpl())
    }
}