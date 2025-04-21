package com.example.fitnesstracker.NavigationApp.home.suggestedExercise

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RecommendedExerciseEntity::class], version = 2)
abstract class RecommendedExerciseDatabase : RoomDatabase() {
    abstract fun recommendedExerciseDao(): RecommendedExerciseDao

    companion object {
        @Volatile private var INSTANCE: RecommendedExerciseDatabase? = null

        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // إضافة عمود إلى جدول exercises
                database.execSQL("ALTER TABLE exercises ADD COLUMN duration INTEGER DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): RecommendedExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendedExerciseDatabase::class.java,
                    "recommended_exercise_db"
                )
                    .addMigrations(migration_1_2) // إضافة الهجرة هنا
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
