package com.example.todo

data class Todo (
    val title: String,
    val description: String,
    var isDone: Boolean = false
)