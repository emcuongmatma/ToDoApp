package com.example.todoapp.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {
    @Provides
    @Singleton
    fun provideTodoAppAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
