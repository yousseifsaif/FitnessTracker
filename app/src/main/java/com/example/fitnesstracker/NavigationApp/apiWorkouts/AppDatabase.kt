package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fitnesstracker.ToolBarIcons.Nottifications.NotificationDao
import com.example.fitnesstracker.ToolBarIcons.Nottifications.NotificationEntity

@Database(
    entities = [ExerciseEntity::class, NotificationEntity::class], version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "fitness_tracker_db"
                ).addMigrations(migration_1_2, migration_2_3).build()
                INSTANCE = instance
                instance
            }
        }

        // Migration from version 1 to 2 (for exercises table)
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE exercises ADD COLUMN duration INTEGER DEFAULT 0")
            }
        }

        // Migration from version 2 to 3 (add notifications table)
        val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS notifications (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        message TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
