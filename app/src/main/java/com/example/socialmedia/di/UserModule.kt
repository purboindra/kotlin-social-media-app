package com.example.socialmedia.di

import android.content.Context
import com.example.socialmedia.data.datasource_impl.UserDatasourceImpl
import com.example.socialmedia.domain.repository.UserRepository
import com.example.socialmedia.domain.repository_impl.UserRepositoryImpl
import com.example.socialmedia.domain.usecases.UserUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideUserDataSourceImpl(@ApplicationContext context: Context): UserDatasourceImpl {
        return UserDatasourceImpl(
            SupabaseModule.provideSupabaseClient(),
            DataStoreModule.provideDataStore(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideUserRepositoryImpl(@ApplicationContext context: Context): UserRepositoryImpl {
        return UserRepositoryImpl(
            provideUserDataSourceImpl(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideUserUseCase(@ApplicationContext context: Context): UserUsecase {
        return UserUsecase(provideUserRepositoryImpl(context))
    }
}