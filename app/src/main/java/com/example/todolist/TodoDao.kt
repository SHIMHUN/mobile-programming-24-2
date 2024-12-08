package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// 데이터베이스에 접근할수있는 객체를 만듬(Dao) - CRUD
@Dao
interface TodoDao {

    // database table에 삽입(추가)
    @Insert
    fun insertTodoData(todoInfo: TodoInfo)

    // database table에 기존에 존재하는 데이터를 수정
    @Update
    fun updateTodoDate(todoInfo: TodoInfo)

    // database table에 기존에 존재하는 데이터를 삭제
    @Delete
    fun deleteTodoDate(todoInfo: TodoInfo)

    // database table에 전체 데이터를 가져옴 (read-조회)
    @Query("SELECT * FROM todo_table") // 전체데이터 조회한다 TodoInfo테이블로부터
    fun getAllReadData(): List<TodoInfo>
}