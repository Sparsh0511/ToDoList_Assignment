package com.example.todolistassignment.LocalDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoItem::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract val dao: ToDoDao
}