package com.example.socialmedia.di

import com.example.socialmedia.data.datasource.AuthDatasource
import com.example.socialmedia.data.datasource_impl.AuthDataSourceImpl
import com.example.socialmedia.domain.repository_impl.AuthRepositoryImpl
import com.example.socialmedia.domain.usecases.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthDatasourceImpl(): AuthDataSourceImpl {
        return AuthDataSourceImpl(SupabaseModule.provideSupabaseClient())
    }
    
    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(): AuthRepositoryImpl {
        return AuthRepositoryImpl(provideAuthDatasourceImpl())
    }
    
    @Provides
    @Singleton
    fun provideAuthUseCase(): AuthUseCase {
        return AuthUseCase(provideAuthRepositoryImpl())
    }
}