package com.example.todolist.data.repository

import com.example.todolist.data.db.TodoInfo

interface Repository {
    fun updateTodo(todoData: TodoInfo): Boolean
    fun deleteTodo(todoData: TodoInfo): Boolean
    fun insertTodo(todoData: TodoInfo): Boolean
    fun getAllTodo(): List<TodoInfo>
}