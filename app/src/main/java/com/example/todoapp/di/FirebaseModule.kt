package com.example.todoapp.di
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule{
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun instance(): FirebaseFirestore {
        return instance
    }
}