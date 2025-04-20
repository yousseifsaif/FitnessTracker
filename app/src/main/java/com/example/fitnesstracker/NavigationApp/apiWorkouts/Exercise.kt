package com.example.fitnesstracker.NavigationApp.apiWorkouts



data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val gifUrl: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    var isFavorite: Boolean = false,


)
{
    constructor() : this("", "", "", "", "", "", listOf(), listOf(), false)
}