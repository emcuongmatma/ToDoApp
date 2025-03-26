package com.example.todoapp.data



sealed class Result<out T> {
    data class Success<out T>(val data: T):Result<T>()
    data class Error(val exception: String):Result<Nothing>()
    data class Loading(val boolean: Boolean):Result<Nothing>()
}