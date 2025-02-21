package com.example.todolistassignment

import com.example.todolistassignment.Model.ToDoItem

interface ToDoItemClickListener {
    fun onItemClick(position: Int, data: ToDoItem)

    fun onItemLongClick(position: Int, data: ToDoItem)
}