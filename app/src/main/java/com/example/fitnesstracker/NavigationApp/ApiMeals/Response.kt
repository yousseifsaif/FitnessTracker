package com.example.fitnesstracker.NavigationApp.ApiMeals


data class EdamamResponse(
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)

data class Recipe(
    val label: String,
    val image: String,
    val url: String,
    val calories: Double,
    val description: String?

)
