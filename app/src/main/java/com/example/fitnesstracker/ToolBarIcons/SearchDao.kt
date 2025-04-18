package com.example.fitnesstracker.ToolBarIcons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query(
        """
        SELECT * FROM exercises 
        WHERE name LIKE '%' || :query || '%'
        OR bodyPart LIKE '%' || :query || '%'
        OR equipment LIKE '%' || :query || '%'
        LIMIT 20
    """
    )
    suspend fun searchExercises(query: String): List<ExerciseEntity>

    @Query("SELECT * FROM recentsearch ORDER BY timestamp DESC LIMIT 5")
    fun getRecentSearches(): Flow<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExercises(exercises: List<ExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: RecentSearch)

    @Delete
    suspend fun deleteSearch(search: RecentSearch)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("DELETE FROM recentsearch WHERE timestamp < :threshold")
    suspend fun deleteOldSearches(threshold: Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)
}