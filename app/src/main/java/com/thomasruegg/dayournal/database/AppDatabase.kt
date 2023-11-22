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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [JournalEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun journalDao(): JournalDao

    private class JournalDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.journalDao())
                }
            }
        }

        suspend fun populateDatabase(journalDao: JournalDao) { // suspend is needed!
            // Delete all content
            journalDao.deleteAll()

            // Add sample entries
            var entry = JournalEntry(
                "2023-10-25",
                "This morning felt like a breath of fresh air. The birds were chirping, and the sun peeked through the curtains as I slowly woke up. For breakfast, I had a wholesome oatmeal bowl topped with fruits and a touch of honey. Though work usually pervades my thoughts, today was different. I took a gentle walk in the garden, feeling the cool breeze on my face. The tranquility brought a sense of clarity, making me feel present and grateful. Even a brief escape from the digital screens felt rejuvenating.",
                4.5F,
                4F,
                3F)
            journalDao.insert(entry)
            entry = JournalEntry(
                "2023-10-26",
                "Today was a day of culinary exploration. Inspired by a cooking show, I decided to try my hand at making homemade pasta. The process was therapeutic, rolling out the dough and cutting it into fettuccine. The result was a delicious plate of pasta, which I enjoyed with a glass of wine. Though the process kept me on my feet, it wasn't a vigorous activity. The laughter and stories shared with my roommate over dinner filled the room with warmth, a pleasant deviation from the routine.",
                4F,
                4.5F,
                2.5F)
            journalDao.insert(entry)
            entry = JournalEntry(
                "2023-10-27",
                "I discovered a lively dance class in the neighborhood and decided to join on a whim. The energetic beats and rhythmic movements took over, making me forget about the day's stresses. For those moments, I was lost in the rhythm, feeling every beat. I hadn't realized how much I missed dancing. After the class, I grabbed a smoothie which was nutritious but slightly on the lighter side. Dancing invigorated my soul and provided a full-body workout, a perfect blend of joy and movement.",
                5F,
                3.5F,
                5F)
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
