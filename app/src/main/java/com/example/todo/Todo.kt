package com.example.todo

import com.google.firebase.database.Exclude

data class Todo (
    val title: String? = null,
    var isDone: Boolean = false

) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "isDone" to isDone
        )
    }
}