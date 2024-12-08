package com.example.todoapp.di

import com.example.todoapp.data.repositories.Api
import com.example.todoapp.data.repositories.ApiImpl
import com.example.todoapp.data.repositories.MainLog
import com.example.todoapp.data.repositories.MainLogImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract fun bindMainLog(
        log : MainLogImpl
    ): MainLog

    @Binds
    @Singleton
    abstract fun bindApi(
        api: ApiImpl
    ): Api
}

