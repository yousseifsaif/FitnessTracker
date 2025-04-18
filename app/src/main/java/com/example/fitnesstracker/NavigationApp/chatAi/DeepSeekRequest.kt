package com.example.fitnesstracker.NavigationApp.chatAi

// DeepSeekRequest.kt
data class DeepSeekRequest(
    val model: String = "deepseek-v3",
    val messages: List<Message>,
    val stream: Boolean = false
)

// Message.kt
data class Message(
    val role: String, // "system", "user" أو "assistant"
    val content: String
)

