package com.example.todolistassignment.LocalDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert
    suspend fun upsertItem(toDoItem: ToDoItem)

    @Update
    suspend fun updateItem(toDoItem: ToDoItem)

    @Delete
    suspend fun deleteItem(toDoItem: ToDoItem)

    @Query("SELECT * FROM ToDoItem")
    fun getToDoItems(): Flow<List<ToDoItem>>
}