package com.example.todolist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

    @Insert
    fun insertTodoData(todoInfo: TodoInfo)

    @Update
    fun updateTodoDate(todoInfo: TodoInfo)

    @Delete
    fun deleteTodoDate(todoInfo: TodoInfo)

    @Query("SELECT * FROM todo_table")
    fun getAllReadData(): List<TodoInfo>
}