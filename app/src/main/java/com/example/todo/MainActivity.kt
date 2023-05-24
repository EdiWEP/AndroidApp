package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView : RecyclerView
    private lateinit var btnAddTodo: Button
    private lateinit var searchView: SearchView

    private lateinit var todoAdapter: TodoAdapter

    private lateinit var todos: MutableList<Todo>
    private lateinit var displayedTodos: MutableList<Todo>

    private fun setupNavBar(binding: ActivityMainBinding) {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getMockTodos() : MutableList<Todo> {
        todos = mutableListOf<Todo>()
        todos.addAll(
            listOf(
                Todo("Task 1", "task"),
                Todo("Task 2", "task"),
                Todo("Task 3", "task")
            )
        )

        return todos
    }

    private fun initializeRecyclerView(todos: MutableList<Todo>) : RecyclerView{
        recyclerView = findViewById(R.id.recyclerviewTodo)
        todoAdapter = TodoAdapter(todos, this)
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        return recyclerView
    }

    private fun initializeSearchView(recyclerView: RecyclerView) {
        searchView = findViewById(R.id.searchView)
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchText = newText!!.lowercase(Locale.getDefault())
                displayedTodos.clear()

                if (searchText.isNotEmpty()) {

                    displayedTodos.addAll(
                        todos.filter {
                            todo -> todo.title.contains(searchText, ignoreCase = true)
                        }
                    )

                }
                else {
                    displayedTodos.addAll(todos)
                }

                recyclerView.adapter!!.notifyDataSetChanged()
                return false
            }
        })
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavBar(binding)

        todos = getMockTodos()
        displayedTodos = mutableListOf<Todo>()
        displayedTodos.addAll(todos)

        recyclerView = initializeRecyclerView(displayedTodos)

        initializeSearchView(recyclerView)

        setEventListeners()
    }
}