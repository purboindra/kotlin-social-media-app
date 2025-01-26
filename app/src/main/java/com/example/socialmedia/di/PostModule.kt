package com.example.socialmedia.di

import android.content.Context
import com.example.socialmedia.data.datasource_impl.PostDataSourceImpl
import com.example.socialmedia.domain.repository_impl.PostRepositoryImpl
import com.example.socialmedia.domain.usecases.PostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {
    @Provides
    @Singleton
    fun providePostDataSourceImpl(@ApplicationContext context: Context): PostDataSourceImpl {
        return PostDataSourceImpl(
            SupabaseModule.provideSupabaseClient(),
            DataStoreModule.provideDataStore(context)
        )
    }
    
    @Provides
    @Singleton
    fun providePostRepositoryImpl(@ApplicationContext context: Context): PostRepositoryImpl {
        return PostRepositoryImpl(providePostDataSourceImpl(context))
    }
    
    @Provides
    @Singleton
    fun providePostUseCase(@ApplicationContext context: Context): PostUseCase {
        return PostUseCase(providePostRepositoryImpl(context))
    }
}