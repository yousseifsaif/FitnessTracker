package com.example.fitnesstracker.ToolBarIcons

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import com.example.fitnesstracker.ToolBarIcons.RecentSearch
import com.example.fitnesstracker.ToolBarIcons.SearchDao

@Database(
    entities = [ExerciseEntity::class, RecentSearch::class], // ضيف كل الكيانات هنا
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun searchDao(): SearchDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}