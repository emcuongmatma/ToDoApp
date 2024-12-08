package com.example.todoapp.common.enum

sealed class LoadStatus(val description: String=""){
    class Intit : LoadStatus()
    class Loading : LoadStatus()
    class Success : LoadStatus()
    class Error (error :String) : LoadStatus(error)
}