package com.example.todoapp.data.repositories

import com.example.todoapp.model.Task

interface Api {
    suspend fun isAuthenticated():Boolean
    suspend fun getAuthState():Boolean
    suspend fun login(username: String, password : String) : Boolean
    suspend fun signup(username: String, password : String,cpwd: String) : Boolean
    suspend fun loadTasks(): List<Task>
    suspend fun addTask(title : String )
    suspend fun updateTask(taskId: String,title:String,createdDate:String,isCompleted:Boolean)
    suspend fun delTask(taskId : String)
}