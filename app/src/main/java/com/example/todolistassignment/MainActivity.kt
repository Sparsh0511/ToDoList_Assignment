package com.example.todolistassignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todolistassignment.LocalDb.Database
import com.example.todolistassignment.Model.ToDoItem
import com.example.todolistassignment.Utilities.Mapper
import com.example.todolistassignment.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ToDoItemClickListener {
    lateinit var binding: ActivityMainBinding

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "product.db"
        ).build()
    }

    private val viewModel by viewModels<ToDoViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ToDoViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.addTask.setOnClickListener {
            val dialog = AddTaskFragment(DialogListenerImpl(viewModel))
            dialog.show(supportFragmentManager, "Add Task")
        }
        binding.itemsList.layoutManager = LinearLayoutManager(this)
        val adapter = ToDoItemListAdapter(emptyList(), viewModel, this)
        binding.itemsList.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoList.collect {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                }
            }
        }


    }

    override fun onItemClick(position: Int, data: ToDoItem) {
        val dialog = AddTaskFragment(data, DialogListenerImpl(viewModel))
        dialog.show(supportFragmentManager, "Update Task")
    }

    override fun onItemLongClick(position: Int, data: ToDoItem) {
        Toast.makeText(this, "Long Press", Toast.LENGTH_SHORT).show()

        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Item")
            .setIcon(R.drawable.delete)
            .setMessage("Are you sure you want to delete this item?")
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog on "No"
            }
            .setPositiveButton("Yes") { dialog, _ ->
//                adapter.removeItem(position) // Remove item from adapter on "Yes"
                viewModel.deleteTaskFromDb(Mapper.fromItemToDb(data))
                dialog.dismiss() // Dismiss the dialog
            }
            .show()
    }


}