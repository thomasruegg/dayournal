package com.thomasruegg.dayournal.dao

import com.thomasruegg.dayournal.model.JournalEntry
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Query("SELECT * FROM journal_entries")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("""
    SELECT 
        CASE :wellnessRating
            WHEN 'sentimentRating' THEN AVG(sentimentRating)
            WHEN 'nutritionRating' THEN AVG(nutritionRating)
            WHEN 'movementRating' THEN AVG(movementRating)
            ELSE NULL
        END
    FROM journal_entries
    """)
    fun getAverageWellnessMetric(wellnessRating: String): Flow<Float>

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    fun getSpecificDateEntry(date: String): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: JournalEntry): Long // used to have suspend here

    @Update
    fun update(entry: JournalEntry): Int // used to have suspend here

    @Delete
    fun delete(entry: JournalEntry): Int // used to have suspend here

    @Query("DELETE FROM journal_entries")
    fun deleteAll(): Int // used to have suspend here
}