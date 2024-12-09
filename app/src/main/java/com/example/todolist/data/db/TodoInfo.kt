package com.example.todolist.data.db

import android.webkit.WebSettings.RenderPriority
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class TodoInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val priority: Int
) {
    fun getPriorityString(priority: Int): String {
        return when (priority) {
            2 -> "상"
            1 -> "중"
            0 -> "하"
            else -> ""
        }
    }
}