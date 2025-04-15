package com.example.fitnesstracker.NavigationApp.apiWorkouts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}
