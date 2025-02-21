package com.example.todolistassignment.Utilities

import com.example.todolistassignment.Model.ToDoItem

class Mapper {

    companion object {

        fun fromItemToDb(item: ToDoItem): com.example.todolistassignment.LocalDb.ToDoItem {
            return com.example.todolistassignment.LocalDb.ToDoItem(
                name = item.name,
                description =  item.description,
                isCompleted = item.isCompleted,
                id = item.serialNum
            )
        }

        fun fromDbToItem(item: com.example.todolistassignment.LocalDb.ToDoItem): ToDoItem {
            return ToDoItem(
                name = item.name,
                description = item.description,
                isCompleted = item.isCompleted,
                serialNum = item.id
            )
        }

    }
}