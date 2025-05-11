package com.example.fitnesstracker.NavigationApp.ApiMeals.room

import android.content.Context
import androidx.room.Room

object MealDatabaseInstance {
    @Volatile
    private var INSTANCE: MealDatabase? = null

    fun getDatabase(context: Context): MealDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext, MealDatabase::class.java, "meals_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
