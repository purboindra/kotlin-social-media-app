package com.example.socialmedia.di

import com.example.socialmedia.data.datasource_impl.UserDatasourceImpl
import com.example.socialmedia.domain.repository.UserRepository
import com.example.socialmedia.domain.repository_impl.UserRepositoryImpl
import com.example.socialmedia.domain.usecases.UserUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideUserDataSourceImpl(): UserDatasourceImpl {
        return UserDatasourceImpl(
            SupabaseModule.provideSupabaseClient(),
        )
    }
    
    @Provides
    @Singleton
    fun provideUserRepositoryImpl(): UserRepositoryImpl {
        return UserRepositoryImpl(
            provideUserDataSourceImpl()
        )
    }
    
    @Provides
    @Singleton
    fun provideUserUseCase(): UserUsecase {
        return UserUsecase(provideUserRepositoryImpl())
    }
}