package com.example.fitnesstracker.NavigationApp.apiWorkouts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val gifUrl: String,
    val secondaryMuscles: String,
    val instructions: String,
    var isFavorite: Boolean = false,
)