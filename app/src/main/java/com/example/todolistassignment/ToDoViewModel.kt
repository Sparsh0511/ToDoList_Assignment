package com.example.todolistassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolistassignment.LocalDb.ToDoDao
import com.example.todolistassignment.Model.ToDoItem
import com.example.todolistassignment.Utilities.Mapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoViewModel(
    val dao: ToDoDao,
): ViewModel() {

    private val _todoList = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todoList: StateFlow<List<ToDoItem>> = _todoList.asStateFlow()
    private val _filterType = MutableStateFlow(FilterType.ALL) // User-selected filter

    private val completedTasks = _todoList.map { list -> list.filter { it.isCompleted } }
    private val uncompletedTasks = _todoList.map { list -> list.filter { !it.isCompleted } }

    val filteredTasks = combine(_todoList, _filterType) { list, filter ->
        when (filter) {
            FilterType.ALL -> list
            FilterType.COMPLETED -> list.filter { it.isCompleted }
            FilterType.UNCOMPLETED -> list.filter { !it.isCompleted }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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

    fun setFilter(filter: FilterType) {
        _filterType.value = filter
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