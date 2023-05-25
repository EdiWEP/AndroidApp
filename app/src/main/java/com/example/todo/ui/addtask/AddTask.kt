package com.example.todo.ui.addtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todo.R
import com.example.todo.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTask : Fragment() {

    private lateinit var btnAddTodo: Button
    private lateinit var etTaskTitle: EditText
    private lateinit var db: FirebaseDatabase
    private lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        db = FirebaseDatabase.getInstance("https://todolistapp-f8ef0-default-rtdb.europe-west1.firebasedatabase.app/")
        dbref = db.getReference("users")

        initializeElements(view)
        return view
    }

    private fun initializeElements(view: View) {
        btnAddTodo = view.findViewById(R.id.btnAddTodo)
        etTaskTitle = view.findViewById(R.id.etTaskTitle)

        btnAddTodo.setOnClickListener {
            val title = etTaskTitle.text.toString()
            if (title.isEmpty()) {
                Toast.makeText(view.context, "Please enter a task name", Toast.LENGTH_SHORT).show()
            } else {
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val todo = Todo(title)
                val key = dbref.child(uid).push().key!!

                var map = hashMapOf<String, Any>()
                map[key] = todo
                dbref.child(uid).updateChildren(map)

                etTaskTitle.text.clear()
                Toast.makeText(view.context, "Task added", Toast.LENGTH_SHORT)

            }
        }
    }
}