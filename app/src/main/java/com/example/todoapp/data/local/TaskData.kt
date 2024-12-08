package com.example.todoapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskdata")
data class TaskData(
    @PrimaryKey val taskId: String ="",
    @ColumnInfo(name = "title") val title: String="",
    @ColumnInfo(name = "createdAt") val createdAt: String="",
    @ColumnInfo(name = "Completed") val completed:Boolean=false
)