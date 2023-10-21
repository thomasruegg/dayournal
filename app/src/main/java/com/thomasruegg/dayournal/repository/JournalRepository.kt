package com.thomasruegg.dayournal.repository

import androidx.annotation.WorkerThread
import com.thomasruegg.dayournal.dao.JournalDao
import com.thomasruegg.dayournal.model.JournalEntry
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 *
 */

class JournalRepository(private val journalDao: JournalDao) {

    // When using a Flow object, Room already executes this on a background thread
    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()

    fun getSpecificDateEntry(date: String): Flow<List<JournalEntry>> {
        return journalDao.getSpecificDateEntry(date)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread // No Flow object, I need to write this annotation
    suspend fun insert(entry: JournalEntry) {
        journalDao.insert(entry)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(entry: JournalEntry) {
        journalDao.update(entry)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(entry: JournalEntry) {
        journalDao.delete(entry)
    }

}
