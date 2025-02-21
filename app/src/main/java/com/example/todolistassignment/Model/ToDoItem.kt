package com.example.todolistassignment.Model

data class ToDoItem(
    val name: String,
    val description: String,
    val isCompleted: Boolean,
    val serialNum: Int?
)
