package com.example.todolistassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.todolistassignment.Model.ToDoItem
import com.example.todolistassignment.databinding.AddTaskBinding

class AddTaskFragment(
    var dialogListener: DialogListener,

): DialogFragment() {

    constructor(item: ToDoItem, dialogListener: DialogListener): this(dialogListener) {
        task = item
    }

    lateinit var binding: AddTaskBinding
    var task: ToDoItem? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTaskBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(task != null) {
            binding.nameEditText.setText(task?.name)
            binding.descriptionEditText.setText(task?.description)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.
        window?.
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onResume() {
        super.onResume()
        binding.submit.setOnClickListener {

            if(task != null) {
                // call update on submit
                if (checkNameError() && checkDescriptionError()) {
                    dialogListener.onUpdateData(
                        ToDoItem(
                            name = binding.nameEditText.text.toString(),
                            description = binding.descriptionEditText.text.toString(),
                            isCompleted = task?.isCompleted == true,
                            serialNum = task?.serialNum
                        )
                    )
                    dialog?.cancel()
                }
            } else {

                if (checkNameError() && checkDescriptionError()) {
                    dialogListener.onSubmitData(
                        ToDoItem(
                            name = binding.nameEditText.text.toString(),
                            description = binding.descriptionEditText.text.toString(),
                            isCompleted = false,
                            serialNum = null
                        )
                    )

                    dialog?.cancel()
                }

            }
        }
    }

    fun checkNameError(): Boolean {
        if(binding.nameEditText.text.toString().isEmpty()) {
            binding.nameInput.error = "Field is required"
            return false
        } else {
            binding.nameInput.error = ""
            return true
        }
    }

    fun checkDescriptionError(): Boolean {
        if(binding.descriptionEditText.text.toString().isEmpty()) {
            binding.descriptionInput.error = "Field is required"
            return false
        } else {
            binding.descriptionInput.error = ""
            return true
        }
    }

    interface DialogListener {
        fun onSubmitData(data: ToDoItem)

        fun onUpdateData(data: ToDoItem)
    }
}