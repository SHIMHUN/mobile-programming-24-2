package com.example.todolist.ui.common

import com.example.todolist.data.db.TodoInfo

interface AdapterListener {
    fun navigateToTaskDetail(todoInfo: TodoInfo)
    suspend fun requestToDeleteItem(item: TodoInfo): Boolean
}