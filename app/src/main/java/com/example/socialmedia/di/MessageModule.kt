package com.example.socialmedia.di

import android.content.Context
import com.example.socialmedia.data.datasource_impl.MessageDatasourceImpl
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.domain.repository_impl.MessageRepositoryImpl
import com.example.socialmedia.domain.usecases.MessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MessageModule {
    
    @Provides
    @Singleton
    fun provideMessageDataSourceImpl(@ApplicationContext context: Context): MessageDatasourceImpl {
        return MessageDatasourceImpl(
            SupabaseModule.provideSupabaseClient(),
            DataStoreModule.provideDataStore(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideMessageRepositoryImpl(@ApplicationContext context: Context): MessageRepositoryImpl {
        return MessageRepositoryImpl(
            provideMessageDataSourceImpl(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideMessageUseCase(@ApplicationContext context: Context): MessageUseCase {
        return MessageUseCase(
            provideMessageRepositoryImpl(context)
        )
    }
    
}