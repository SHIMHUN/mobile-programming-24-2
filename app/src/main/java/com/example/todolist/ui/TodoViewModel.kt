package com.example.todolist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.db.TodoInfo
import com.example.todolist.data.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: Repository) : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoInfo>>()
    val todoList: LiveData<List<TodoInfo>>
        get() = _todoList

    private val _selectedTodoInfo = MutableLiveData<TodoInfo>()
    val selectedTodo: LiveData<TodoInfo>
        get() = _selectedTodoInfo

    private fun checkTodoInfo(todoInfo: TodoInfo): Boolean {
        return todoInfo.title.isNotBlank() &&
                todoInfo.description.isNotBlank() &&
                todoInfo.date.isNotBlank() &&
                todoInfo.time.isNotBlank() &&
                todoInfo.priority != -1
    }

    private fun getPriorityInt(priority: String): Int {
        return when (priority) {
            "상" -> 2
            "중" -> 1
            "하" -> 0
            else -> -1
        }
    }

    suspend fun deleteTodoData(todoInfo: TodoInfo): Boolean {
        return if(viewModelScope.async { repository.deleteTodo(todoInfo) }.await()) {
            requestTodoList()
            true
        }
        else {
            false
        }
    }

    suspend fun addTodoData(
        text: String,
        description: String,
        date: String,
        time: String,
        priority: String
    ): Boolean {
        return viewModelScope.async {
            val todoInfo = TodoInfo(0, text, description, date, time, getPriorityInt(priority))
            if (checkTodoInfo(todoInfo)) {
                repository.insertTodo(todoInfo)
            } else {
                false
            }
        }.await()
    }

    fun requestTodoList() {
        viewModelScope.launch {
            _todoList.value = repository.getAllTodo()
        }
    }

    fun getSelectedTodoInfo(id: Int): TodoInfo? {
        return _todoList.value?.let { it.find { info -> info.id == id } }
    }
}