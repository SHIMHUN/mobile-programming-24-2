package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class TodoInfo {
    var todoContent : String = ""
    var todoDate : String = ""

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}