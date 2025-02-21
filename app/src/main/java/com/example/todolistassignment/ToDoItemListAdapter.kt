package com.example.todolistassignment

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistassignment.Model.ToDoItem
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class ToDoItemListAdapter(
    var list: List<ToDoItem>,
    val viewModel: ToDoViewModel,
    val toDoItemListener: ToDoItemClickListener
) : RecyclerView.Adapter<ToDoItemListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<MaterialTextView>(R.id.name)
        val description = view.findViewById<MaterialTextView>(R.id.description)
        val checkBox = view.findViewById<MaterialCheckBox>(R.id.checkBox)
        val serialNum = view.findViewById<MaterialTextView>(R.id.serial)
        val root = view.findViewById<MaterialCardView>(R.id.rootCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.description.text = item.description
//        holder.serialNum.text = item.serialNum.toString()
        holder.serialNum.visibility = View.GONE

        if (item.isCompleted) {
            holder.name.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.description.paintFlags = holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.checkBox.checkedState = MaterialCheckBox.STATE_CHECKED
        } else {
            holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.description.paintFlags = holder.description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.checkBox.checkedState = MaterialCheckBox.STATE_UNCHECKED
        }

        holder.checkBox.addOnCheckedStateChangedListener { checkBox, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                viewModel.updateTaskInDb(
                    com.example.todolistassignment.LocalDb.ToDoItem(
                        name = item.name,
                        description = item.description,
                        isCompleted = true,
                        id = item.serialNum
                    )
                )
            } else {
                viewModel.updateTaskInDb(
                    com.example.todolistassignment.LocalDb.ToDoItem(
                        name = item.name,
                        description = item.description,
                        isCompleted = false,
                        id = item.serialNum
                    )
                )
            }
        }

        holder.root.setOnClickListener {
            /*val dialog = AddTaskFragment(DialogListenerImpl(viewModel))
            dialog.show(context, "Update Task")*/
            toDoItemListener.onItemClick(position, item)
        }

        holder.root.setOnLongClickListener {
            toDoItemListener.onItemLongClick(position, item)
            true
        }
    }

}