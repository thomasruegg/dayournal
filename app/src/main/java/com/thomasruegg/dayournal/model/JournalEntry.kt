package com.thomasruegg.dayournal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey()
    val date: String,
    val content: String,
    val sentimentRating: Float,
    val nutritionRating: Float,
    val movementRating: Float
)
