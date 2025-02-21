package com.example.todolistassignment.LocalDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val description: String,
    val isCompleted: Boolean
)
