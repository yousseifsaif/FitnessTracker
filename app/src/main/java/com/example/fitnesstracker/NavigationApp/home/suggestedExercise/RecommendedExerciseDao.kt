package com.example.fitnesstracker.NavigationApp.home.suggestedExercise

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecommendedExerciseDao {
    @Query("SELECT * FROM recommended_exercises LIMIT 10")
    suspend fun getTopExercises(): List<RecommendedExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<RecommendedExerciseEntity>)

    @Query("DELETE FROM recommended_exercises")
    suspend fun clearAll()
}