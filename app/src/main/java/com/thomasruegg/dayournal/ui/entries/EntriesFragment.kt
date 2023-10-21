package com.thomasruegg.dayournal.ui.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import androidx.lifecycle.ViewModelProvider
import com.thomasruegg.dayournal.DayournalApplication
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.databinding.FragmentEntriesBinding
import com.thomasruegg.dayournal.viewmodel.JournalViewModel
import com.thomasruegg.dayournal.viewmodel.JournalViewModelFactory

class EntriesFragment : Fragment() {

    private var _binding: FragmentEntriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val newJournalActivityRequestCode = 1 // TODO do this whole page again https://developer.android.com/codelabs/android-room-with-a-view-kotlin#15
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((requireActivity().application as DayournalApplication).repository) // requireActivity. added
    }
    private val adapter = JournalEntryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)


//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Add an observer on the LiveData returned by `allEntries`.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        journalViewModel.allEntries.observe(viewLifecycleOwner, Observer { journalEntries -> // viewLifecycleOwner used to be `this`
            // Update the cached copy of the journal entries in the adapter
            journalEntries.let { adapter.submitList(it) }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}