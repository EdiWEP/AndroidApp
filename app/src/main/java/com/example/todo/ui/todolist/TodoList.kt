package com.example.todo.ui.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.Todo
import com.example.todo.TodoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class TodoList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var searchView: SearchView

    private lateinit var todos: MutableList<Todo>
    private lateinit var todoIds: MutableList<String>
    private lateinit var displayedTodos: MutableList<Todo>
    private lateinit var db: FirebaseDatabase
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        db = FirebaseDatabase.getInstance("https://todolistapp-f8ef0-default-rtdb.europe-west1.firebasedatabase.app/")
        dbref = db.getReference("users")

        todos = mutableListOf<Todo>()
        todoIds = mutableListOf<String>()
        displayedTodos = mutableListOf<Todo>()
        getTodos()

        recyclerView = initializeRecyclerView(view, displayedTodos)

        initializeSearchView(view, recyclerView)

        return view
    }


    private fun initializeRecyclerView(view: View, todos: MutableList<Todo>) : RecyclerView {
        recyclerView = view.findViewById(R.id.recyclerviewTodo)
        todoAdapter = TodoAdapter(todos, todoIds, view.context)
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        return recyclerView
    }

    private fun initializeSearchView(view: View, recyclerView: RecyclerView) {
        searchView = view.findViewById(R.id.searchView)
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
                                todo -> todo.title!!.contains(searchText, ignoreCase = true)
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

    private fun getTodos() {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref.child(uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todos.clear()
                todoIds.clear()
                if (snapshot.exists()) {
                    for (todoSnapshot in snapshot.children) {
                        val todo = todoSnapshot.getValue(Todo::class.java)
                        todos.add(todo!!)
                        todoIds.add(todoSnapshot.key!!)
                    }
                    displayedTodos.clear()
                    displayedTodos.addAll(todos)

                    todoAdapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}