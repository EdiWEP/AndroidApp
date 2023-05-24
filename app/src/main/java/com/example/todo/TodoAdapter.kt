package com.example.todo


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class TodoAdapter(
    private val todos: MutableList<Todo>,
    private val context: Context
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodoTitle : TextView = itemView.findViewById(R.id.textviewTodoTitle)
        val checkboxDone: CheckBox = itemView.findViewById(R.id.checkboxDone)
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)

    }

    fun deleteTodo(index: Int) {
        todos.removeAt(index)
        notifyItemRemoved(index)
    }

    private fun toggleCheckboxColor(checkbox: CheckBox) {
        if (checkbox.isChecked) {
            checkbox.buttonTintList = ContextCompat.getColorStateList(context, R.color.green)
        } else{
            checkbox.buttonTintList = ContextCompat.getColorStateList(context, R.color.black)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.tvTodoTitle.text = currentTodo.title
        holder.checkboxDone.isChecked = currentTodo.isDone
        toggleCheckboxColor(holder.checkboxDone)

        holder.checkboxDone.setOnCheckedChangeListener { _, _ ->
            toggleCheckboxColor(holder.checkboxDone)
            currentTodo.isDone = !currentTodo.isDone
        }

    }

    override fun getItemCount(): Int {
        return todos.size
    }
}