package com.example.todo


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class TodoAdapter(
    private val todos: MutableList<Todo>,
    private val todoIds: MutableList<String>,
    private val context: Context
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodoTitle: TextView = itemView.findViewById(R.id.textviewTodoTitle)
        val checkboxDone: CheckBox = itemView.findViewById(R.id.checkboxDone)
        val shareButton: Button = itemView.findViewById(R.id.shareButton)
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

    private fun getTextToShare(holder: TodoViewHolder): String {
        if (holder.checkboxDone.isChecked) {
            return "I completed this task: ${holder.tvTodoTitle.text}"
        }
        else {
            return "I am working on this task: ${holder.tvTodoTitle.text}"
        }
    }
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.tvTodoTitle.text = currentTodo.title
        holder.checkboxDone.isChecked = currentTodo.isDone
        toggleCheckboxColor(holder.checkboxDone)

        holder.checkboxDone.setOnCheckedChangeListener { _, _ ->
            toggleCheckboxColor(holder.checkboxDone)
            currentTodo.isDone = !currentTodo.isDone
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val dbref = FirebaseDatabase
                .getInstance("https://todolistapp-f8ef0-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/$uid/${todoIds[position]}/done")
            dbref.setValue(currentTodo.isDone)

        }

        holder.shareButton.setOnClickListener {
            val textToShare = getTextToShare(holder)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textToShare)
            val chooser = Intent.createChooser(intent, "Share via")
            ContextCompat.startActivity(context, chooser, null)
        }

    }

    override fun getItemCount(): Int {
        return todos.size
    }
}