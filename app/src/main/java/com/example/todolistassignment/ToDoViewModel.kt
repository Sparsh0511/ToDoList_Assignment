package com.example.todolistassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolistassignment.LocalDb.ToDoDao
import com.example.todolistassignment.Model.ToDoItem
import com.example.todolistassignment.Utilities.Mapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoViewModel(
    val dao: ToDoDao,
): ViewModel() {

    private val _todoList = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todoList: StateFlow<List<ToDoItem>> = _todoList.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getToDoItems()
                .map { items -> items.map { Mapper.fromDbToItem(it) } }
                .collect {
                    _todoList.value = it
                }
        }
    }

    private fun fetchTasks() {
        val list = mutableListOf<ToDoItem>()
        viewModelScope.launch {
            dao.getToDoItems()
                .collect{items ->
                    _todoList.update {
                        items.map {
                            Mapper.fromDbToItem(it)
                        }
                    }
                }
        }
    }

    fun saveTaskToDb(item: com.example.todolistassignment.LocalDb.ToDoItem) {
        viewModelScope.launch {
            dao.upsertItem(item)
        }
    }

    fun updateTaskInDb(item: com.example.todolistassignment.LocalDb.ToDoItem) {
        viewModelScope.launch {
            dao.updateItem(item)
        }
    }

    fun deleteTaskFromDb(item: com.example.todolistassignment.LocalDb.ToDoItem) {
        viewModelScope.launch {
            dao.deleteItem(item)
        }
    }
}