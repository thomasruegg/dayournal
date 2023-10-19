package com.thomasruegg.dayournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thomasruegg.dayournal.model.JournalEntry
import com.thomasruegg.dayournal.repository.JournalRepository
import kotlinx.coroutines.launch

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {

    /*
     Using LiveData and caching what allWords returns has several benefits:
     - I can put an observer on the data (instead of polling for changes) and only update the
       the UI when the data actually changes.
     - Repository is completely separated from the UI through the ViewModel.
     */
    val allEntries: LiveData<List<JournalEntry>> = repository.allEntries.asLiveData()

    suspend fun getSpecificDateEntry(date: String): LiveData<List<JournalEntry?>> { // TODO weird. not sure if suspend should to be there
        return repository.getSpecificDateEntry(date).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entry: JournalEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun update(entry: JournalEntry) = viewModelScope.launch {
        repository.update(entry)
    }

    fun delete(entry: JournalEntry) = viewModelScope.launch {
        repository.delete(entry)
    }
}

class JournalViewModelFactory(private val repository: JournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
