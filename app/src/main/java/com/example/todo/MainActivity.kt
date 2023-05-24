package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ui.addtask.AddTask
import com.example.todo.ui.archive.Archive
import com.example.todo.ui.todolist.TodoList
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnAddTodo: Button
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todos: MutableList<Todo>

    private fun setupNavBar(binding: ActivityMainBinding) {
        val navView: BottomNavigationView = binding.navView
        replaceFragment(TodoList())

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_todolist -> replaceFragment(TodoList())
                R.id.nav_add_task -> replaceFragment(AddTask())
                R.id.nav_archive -> replaceFragment(Archive())

                else -> {}
            }
            true
        }
    }


    private fun setEventListeners() {
        btnAddTodo = findViewById(R.id.btnAddTodo)
        btnAddTodo.setOnClickListener {
            val editText : EditText = findViewById<EditText>(R.id.editTextTodoTitle)
            val todoTitle = editText.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle, "default description")
                todoAdapter.addTodo(todo)
                editText.text.clear()

                // TODO: change with better persistence method
                todos.add(todo)
            }


        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavBar(binding)

    }
}