package com.example.fitnesstracker.NavigationApp.home.suggestedExercise

data class RecommendedExercise(
    val id: String, val name: String, val gifUrl: String

)

fun RecommendedExercise.toEntity(): RecommendedExerciseEntity =
    RecommendedExerciseEntity(id, name, gifUrl)

fun RecommendedExerciseEntity.toExercise(): RecommendedExercise =
    RecommendedExercise(id, name, gifUrl)