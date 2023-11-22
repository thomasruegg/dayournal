package com.thomasruegg.dayournal.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.database.AppDatabase
import com.thomasruegg.dayournal.databinding.FragmentStatisticsBinding
import com.thomasruegg.dayournal.repository.JournalRepository
import com.thomasruegg.dayournal.viewmodel.JournalViewModel
import com.thomasruegg.dayournal.viewmodel.JournalViewModelFactory

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // This is a bit ugly but I couldn't find a better way to do it
    private fun getWellnessRatingBarId(wellnessName: String): Int {
        return when(wellnessName) {
            "sentimentRating" -> R.id.statisticsSentimentRatingBar
            "nutritionRating" -> R.id.statisticsNutritionRatingBar
            "movementRating" -> R.id.statisticsMovementRatingBar
            else -> throw IllegalArgumentException("Unknown wellness name: $wellnessName")
        }
    }

    private fun getWellnessMessageId(wellnessName: String): Int {
        return when(wellnessName) {
            "sentimentRating" -> R.id.statisticsSentimentMessage
            "nutritionRating" -> R.id.statisticsNutritionMessage
            "movementRating" -> R.id.statisticsMovementMessage
            else -> throw IllegalArgumentException("Unknown wellness name: $wellnessName")
        }
    }
    private fun bindWellnessFactorToRatingbar(wellnessName: String, view: View) {
        // Obtain JournalViewModel
        val journalViewModel = ViewModelProvider(
            this, JournalViewModelFactory(
                JournalRepository(
                    AppDatabase.getDatabase(
                        requireContext(), lifecycleScope
                    ).journalDao()
                )
            )
        )[JournalViewModel::class.java]

        journalViewModel.getAverageWellnessMetric(wellnessName)
            .observe(viewLifecycleOwner) { wellnessFactor ->
                Log.d("StatisticsFragment", "Average $wellnessName: $wellnessFactor")

                val wellnessRatingBar = getWellnessRatingBarId(wellnessName)
                view.findViewById<RatingBar>(wellnessRatingBar).rating = wellnessFactor

                val wellnessMessage = getWellnessMessageId(wellnessName)
                val ratingString =
                    when (wellnessFactor) {
                        in 0.0..1.0 -> getString(R.string.very_low)
                        in 1.0..2.0 -> getString(R.string.low)
                        in 2.0..3.0 -> getString(R.string.neutral)
                        in 3.0..4.0 -> getString(R.string.high)
                        in 4.0..5.0 -> getString(R.string.very_high)
                        else -> throw IllegalArgumentException("Unknown wellness name: $wellnessName")
                    }
                val translatedWellnessName = when (wellnessName.replace("Rating", "")) {
                    "sentiment" -> getString(R.string.sentiment)
                    "nutrition" -> getString(R.string.nutrition)
                    "movement" -> getString(R.string.movement)
                    else -> throw IllegalArgumentException("Unknown wellness name: $wellnessName")
                }
                view.findViewById<TextView>(wellnessMessage).text =
                    getString(R.string.on_average_your_rating_is, translatedWellnessName, ratingString)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindWellnessFactorToRatingbar("sentimentRating", view)
        bindWellnessFactorToRatingbar("nutritionRating", view)
        bindWellnessFactorToRatingbar("movementRating", view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}