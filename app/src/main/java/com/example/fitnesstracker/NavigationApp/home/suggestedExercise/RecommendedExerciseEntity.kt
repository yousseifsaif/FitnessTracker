package com.example.fitnesstracker.NavigationApp.home.suggestedExercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommended_exercises")
data class RecommendedExerciseEntity(
    @PrimaryKey val id: String, val name: String, val gifUrl: String
)