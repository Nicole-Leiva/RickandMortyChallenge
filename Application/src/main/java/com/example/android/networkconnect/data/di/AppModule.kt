package com.example.android.networkconnect.data.di

import com.example.android.networkconnect.data.repo.AppRepositoryImpl
import com.example.android.networkconnect.data.retrofit.AppRemoteDao
import com.example.android.networkconnect.support.repo.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppRepository(remoteDao: AppRemoteDao) = AppRepositoryImpl(remoteDao) as AppRepository
    /*fun provideAppRepository(remoteDao: AppRemoteDao) : AppRepository{
        return AppRepositoryImpl(remoteDao)
    }*/
}