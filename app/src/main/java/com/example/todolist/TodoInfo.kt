package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class TodoInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,      // 자동 생성 ID
    val todoContent: String,                         // 기본값 설정
    val todoDate: String                             // 기본값 설정
)