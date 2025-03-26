package com.example.todoapp.common.enum

sealed class LoadStatus(val description: String=""){
    class Init : LoadStatus()
    class Loading : LoadStatus()
}