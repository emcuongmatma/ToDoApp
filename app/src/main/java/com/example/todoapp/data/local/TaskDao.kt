package com.example.todoapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM taskdata")
    fun getAll(): List<TaskData>

    @Query("SELECT * FROM taskdata WHERE taskId IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<TaskData>

    @Insert
    fun insertAll(vararg taskData: TaskData)

    @Delete
    fun delete(taskData: TaskData)
}