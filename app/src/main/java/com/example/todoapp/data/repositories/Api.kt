package com.example.todoapp.data.repositories

import com.example.todoapp.data.Result
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

interface Api {
//    suspend fun getAuthState():Result<Boolean>
    suspend fun login(username: String, password : String) : Result<Boolean>
    suspend fun signup(username: String, password : String,cpwd: String) : Result<Boolean>
    fun loadTasks(username: String): Flow<List<Task>>
    suspend fun addTask(title : String,username: String): Result<Unit>
    suspend fun updateTask(task: Task,username: String): Result<Unit>
    suspend fun delTask(taskId : String,username: String): Result<Unit>
    suspend fun getCurrentUser(): Result<String>
}