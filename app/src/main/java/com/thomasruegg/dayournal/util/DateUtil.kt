package com.thomasruegg.dayournal.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtil {
    companion object {
        // if there was a date passed, then 
        fun getDatabaseStringOfToday(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        }
        
        fun getFullDateString(dateString: String): String {
            val date = if (dateString == "today") {
                Date()
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
            }
            val formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())
            return formatter.format(date)
        }
    }
}