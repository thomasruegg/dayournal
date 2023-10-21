package com.thomasruegg.dayournal.ui.write

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.databinding.FragmentWriteBinding
import com.thomasruegg.dayournal.model.JournalEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A fragment for creating new journal entries.
 */
class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getFormattedDateString(): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun greetingMessage() : String {
        val cal = Calendar.getInstance()

        return when(cal.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            in 18..20 -> getString(R.string.good_evening)
            in 21..23 -> getString(R.string.good_night)
            else -> getString(R.string.hello)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val greetingPlaceholder: TextView = binding.greetingPlaceholder
        homeViewModel.text.observe(viewLifecycleOwner) {
            greetingPlaceholder.text = greetingMessage()
        }

        val dateTextView: TextView = binding.dateTextView
        homeViewModel.text.observe(viewLifecycleOwner) {
            dateTextView.text = getFormattedDateString()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalEditText: EditText = view.findViewById(R.id.journalEditText)
        journalEditText.addTextChangedListener(JournalTextWatcher())
    }

    private inner class JournalTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do something before the text changes, if needed
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // Do something as the text is being changed, if needed
        }

        override fun afterTextChanged(s: Editable) {
//            val journalEntryText = s.toString()
            // Now you can use journalEntryText as you wish

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}