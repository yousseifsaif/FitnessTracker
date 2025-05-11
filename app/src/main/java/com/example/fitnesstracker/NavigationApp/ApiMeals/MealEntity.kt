package com.example.fitnesstracker.NavigationApp.ApiMeals.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals_table")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val image: String,
    val calories: Double,
    val description: String?
)