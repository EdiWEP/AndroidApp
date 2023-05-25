package com.example.todo

import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ui.addtask.AddTask
import com.example.todo.ui.archive.Archive
import com.example.todo.ui.todolist.TodoList
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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