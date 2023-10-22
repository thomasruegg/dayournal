package com.thomasruegg.dayournal.ui.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.database.AppDatabase
import com.thomasruegg.dayournal.databinding.FragmentWriteBinding
import com.thomasruegg.dayournal.model.JournalEntry
import com.thomasruegg.dayournal.repository.JournalRepository
import com.thomasruegg.dayournal.util.DateUtil.Companion.getDatabaseStringOfToday
import com.thomasruegg.dayournal.util.DateUtil.Companion.getFullDateString
import com.thomasruegg.dayournal.viewmodel.JournalViewModel
import com.thomasruegg.dayournal.viewmodel.JournalViewModelFactory
import java.util.Calendar

/**
 * A fragment for creating new journal entries.
 */
class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // needed if I want to implement saving when switching fragments
    private lateinit var journalEditText: EditText
    private lateinit var sentimentRatingBar: RatingBar
    private lateinit var nutritionRatingBar: RatingBar
    private lateinit var movementRatingBar: RatingBar
    private lateinit var journalViewModel: JournalViewModel

    private fun greetingMessage(): String {
        val cal = Calendar.getInstance()

        return when (cal.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            in 18..20 -> getString(R.string.good_evening)
            in 21..23 -> getString(R.string.good_night)
            else -> getString(R.string.hello)
        }
    }

    // Save information when user presses save button.
    private fun saveToDatabase(
        journalEditText: EditText,
        sentimentRatingBar: RatingBar,
        nutritionRatingBar: RatingBar,
        movementRatingBar: RatingBar,
        journalViewModel: JournalViewModel,
        view: View,
        showSavedMessage: Boolean = false
    ) {
        val newEntry = JournalEntry(
            content = journalEditText.text.toString(),
            date = getDatabaseStringOfToday(),
            sentimentRating = sentimentRatingBar.rating,
            nutritionRating = nutritionRatingBar.rating,
            movementRating = movementRatingBar.rating
        )
        journalViewModel.insert(newEntry)
        if (showSavedMessage) {
            Snackbar.make(view, "Entry saved", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val greetingPlaceholder: TextView = binding.greetingPlaceholder
        homeViewModel.text.observe(viewLifecycleOwner) {
            greetingPlaceholder.text = greetingMessage()
        }

        // put today's date into the dateTextView
        val dateTextView: TextView = binding.dateTextView
        homeViewModel.text.observe(viewLifecycleOwner) {
            dateTextView.text = getFullDateString("today")
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        journalEditText = view.findViewById(R.id.journalEditText)
        sentimentRatingBar = view.findViewById(R.id.sentimentRatingBar)
        nutritionRatingBar = view.findViewById(R.id.nutritionRatingBar)
        movementRatingBar = view.findViewById(R.id.movementRatingBar)

        // Obtain JournalViewModel
        journalViewModel = ViewModelProvider(
            this, JournalViewModelFactory(
                JournalRepository(
                    AppDatabase.getDatabase(
                        requireContext(), lifecycleScope
                    ).journalDao()
                )
            )
        )[JournalViewModel::class.java]

        // Check database for an existing entry for today and populate the fields if it exists
        journalViewModel.getSpecificDateEntry(getDatabaseStringOfToday())
            .observe(viewLifecycleOwner) { entries ->
                Log.d("WriteFragment", "Entries: $entries")
                if (entries.isNotEmpty()) {
                    if (journalEditText.text.isNotEmpty() || sentimentRatingBar.rating != 0.0f || nutritionRatingBar.rating != 0.0f || movementRatingBar.rating != 0.0f) {
                        // If the user has already filled in some data, don't overwrite it
                        return@observe
                    }
                    journalEditText.setText(entries[0].content)
                    sentimentRatingBar.rating = entries[0].sentimentRating
                    nutritionRatingBar.rating = entries[0].nutritionRating
                    movementRatingBar.rating = entries[0].movementRating
                    Log.d("WriteFragment", "Text set: ${entries[0].content}")
                }
            }

        // Save information when user presses save button.
        val saveButton: FloatingActionButton = view.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveToDatabase(
                journalEditText,
                sentimentRatingBar,
                nutritionRatingBar,
                movementRatingBar,
                journalViewModel,
                requireView(),
                true
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}