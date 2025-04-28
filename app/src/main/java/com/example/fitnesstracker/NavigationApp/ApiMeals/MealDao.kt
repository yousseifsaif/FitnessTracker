package com.example.fitnesstracker.NavigationApp.ApiMeals.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Query("SELECT * FROM meals_table")
    suspend fun getAllMealsOnce(): List<MealEntity>
}
