package com.example.fitnesstracker.ToolBarIcons.Nottifications

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): LiveData<List<NotificationEntity>>

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}
