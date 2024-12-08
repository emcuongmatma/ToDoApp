package com.example.todoapp.di

import android.app.Application
import com.example.todoapp.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): TaskDatabase {
        return TaskDatabase.getDatabase(application)
    }
}