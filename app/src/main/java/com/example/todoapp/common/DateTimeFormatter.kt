package com.example.todoapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentTime():String{
    val currentTime = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentTimeString = dateFormat.format(currentTime)
    return currentTimeString
}