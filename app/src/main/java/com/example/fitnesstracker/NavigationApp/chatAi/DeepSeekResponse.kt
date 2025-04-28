package com.example.fitnesstracker.NavigationApp.chatAi

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)