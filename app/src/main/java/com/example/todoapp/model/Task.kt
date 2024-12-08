package com.example.todoapp.model

data class Task (
    val taskId: String = "",
    var title: String="",
    val createdAt: String="",
    var isCompleted: Boolean=false
)