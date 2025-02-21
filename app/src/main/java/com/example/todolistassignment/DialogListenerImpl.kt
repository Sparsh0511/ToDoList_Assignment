package com.example.todolistassignment

import android.util.Log
import com.example.todolistassignment.Model.ToDoItem
import com.example.todolistassignment.Utilities.Mapper

class DialogListenerImpl(
    val viewModel: ToDoViewModel
): AddTaskFragment.DialogListener {
    override fun onSubmitData(data: ToDoItem) {
        Log.i("DATA", "Name: ${data.name} Description: ${data.description}")
        val item = Mapper.fromItemToDb(data)
        viewModel.saveTaskToDb(item)

    }

    override fun onUpdateData(data: ToDoItem) {
        Log.i("DATA", "Name: ${data.name} Description: ${data.description}")
        val item = Mapper.fromItemToDb(data)
        viewModel.updateTaskInDb(item)

    }
}