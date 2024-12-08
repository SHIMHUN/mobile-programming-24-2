package com.example.todolist

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.TodoInfo

@Database(entities = [TodoInfo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getInstance(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo-database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries() // 임시로 사용
                        .build()
                        .also {
                            INSTANCE = it
                            Log.i("TodoDatabase", "Database created successfully!")
                        }
                } catch (e: Exception) {
                    Log.e("TodoDatabase", "Database creation failed: ${e.stackTrace[0]}")
                    throw e // 에러를 다시 던져 로그 확인
                }
            }
        }
    }
}