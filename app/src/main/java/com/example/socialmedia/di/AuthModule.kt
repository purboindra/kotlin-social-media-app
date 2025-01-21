package com.example.socialmedia.di

import android.content.Context
import com.example.socialmedia.data.datasource.AuthDatasource
import com.example.socialmedia.data.datasource_impl.AuthDataSourceImpl
import com.example.socialmedia.domain.repository_impl.AuthRepositoryImpl
import com.example.socialmedia.domain.usecases.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthDatasourceImpl(@ApplicationContext context: Context): AuthDataSourceImpl {
        return AuthDataSourceImpl(
            SupabaseModule.provideSupabaseClient(),
            DataStoreModule.provideDataStore(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(@ApplicationContext context: Context): AuthRepositoryImpl {
        return AuthRepositoryImpl(provideAuthDatasourceImpl(context))
    }
    
    @Provides
    @Singleton
    fun provideAuthUseCase(@ApplicationContext context: Context): AuthUseCase {
        return AuthUseCase(provideAuthRepositoryImpl(context))
    }
}