package com.example.todoapp.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.Task

class State : ViewModel() {
    val loading = mutableStateOf(false)
    val taskList = mutableListOf<Task>()
    val lazyState = mutableStateOf(LazyScreen.Old)
    val openDialog = mutableStateOf(false)
}
enum class LazyScreen{
    Old,
    Updating,
    Updated
}

