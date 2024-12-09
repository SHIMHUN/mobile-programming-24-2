package com.example.todolist.data.repository

import android.util.Log
import com.example.todolist.data.db.TodoDatabase
import com.example.todolist.data.db.TodoInfo

class RepositoryImpl(private val db: TodoDatabase): Repository {
    override fun updateTodo(todoData: TodoInfo): Boolean {
        try {
            db.todoDao().updateTodoDate(todoData)
            return true
        } catch (e: Throwable) {
            Log.d("Failed to update", e.stackTraceToString())
            return false
        }
    }

    override fun deleteTodo(todoData: TodoInfo): Boolean {
        try {
            db.todoDao().deleteTodoDate(todoData)
            return true
        } catch (e: Throwable) {
            Log.d("Failed to delete", e.stackTraceToString())
            return false
        }
    }

    override fun insertTodo(todoData: TodoInfo): Boolean {
        try {
            db.todoDao().insertTodoData(todoData)
            return true
        } catch (e: Throwable) {
            Log.d("Failed to add", e.stackTraceToString())
            return false
        }
    }

    override fun getAllTodo(): List<TodoInfo> {
        return  try {
            val response = db.todoDao().getAllReadData()
            Log.d("Succeed to get all data", response.toString())
            response
        } catch (e: Throwable) {
            Log.d("Failed to get all data", e.message.toString())
            emptyList()
        }
    }
}