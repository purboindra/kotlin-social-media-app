package com.example.socialmedia.di

import android.content.Context
import com.example.socialmedia.data.datasource_impl.InstaStoryDatasourceImpl
import com.example.socialmedia.domain.repository_impl.InstaStoryRepositoryImpl
import com.example.socialmedia.domain.usecases.InstaStoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InstaStoryModel {
    @Provides
    @Singleton
    fun provideInstaStoryDatasourceImpl(@ApplicationContext context: Context): InstaStoryDatasourceImpl {
        return InstaStoryDatasourceImpl(
            SupabaseModule.provideSupabaseClient(),
            DataStoreModule.provideDataStore(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideInstaStoryRepositoryImpl(@ApplicationContext context: Context): InstaStoryRepositoryImpl {
        return InstaStoryRepositoryImpl(
            provideInstaStoryDatasourceImpl(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideInstaStoryUseCase(@ApplicationContext context: Context): InstaStoryUseCase {
        return InstaStoryUseCase(provideInstaStoryRepositoryImpl(context))
    }
}