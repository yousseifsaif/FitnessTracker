package com.example.fitnesstracker.ToolBarIcons

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
