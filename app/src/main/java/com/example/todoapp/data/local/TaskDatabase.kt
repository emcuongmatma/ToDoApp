package com.example.todoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskData::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: TaskDatabase? = null
        fun getDatabase(context: Context): TaskDatabase {
            if (instance == null) {
                synchronized(TaskDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            TaskDatabase::class.java,
                            "tasks.db"
                        )
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}
