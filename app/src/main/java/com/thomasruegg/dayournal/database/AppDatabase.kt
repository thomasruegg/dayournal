package com.thomasruegg.dayournal.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thomasruegg.dayournal.dao.JournalDao
import com.thomasruegg.dayournal.model.JournalEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [JournalEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun journalDao(): JournalDao

    private class JournalDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.journalDao())
                }
            }
        }

        suspend fun populateDatabase(journalDao: JournalDao) {
            // Delete all content here.
            journalDao.deleteAll()

            // Add sample words.
            var entry = JournalEntry(1, "Hello", "2020-01-01") // todo remove id from constructor, should be autoincrement
            journalDao.insert(entry)
            entry = JournalEntry(2, "World!", "2020-01-02")
            journalDao.insert(entry)
            Log.d("AppDatabase", "Populated database")
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "journal_database"
                )
                    .addCallback(JournalDatabaseCallback(scope))
                    .fallbackToDestructiveMigration() // TODO check if app crashes next time when reinstalling
                    .build()
                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
}
