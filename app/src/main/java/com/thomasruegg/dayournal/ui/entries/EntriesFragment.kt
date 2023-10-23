package com.thomasruegg.dayournal.ui.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((requireActivity().application as DayournalApplication).repository) // requireActivity. added
    }
    private val adapter = JournalEntryListAdapter()

    private fun updateEmptyView(recyclerView: RecyclerView, view: View) {
        val emptyView: TextView = view.findViewById(R.id.emptyViewText)
        if (recyclerView.adapter?.itemCount == 0) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

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
        journalViewModel.allEntries.observe(viewLifecycleOwner) { journalEntries ->
            // Update the cached copy of the journal entries in the adapter
            journalEntries.let { adapter.submitList(it) }
            updateEmptyView(recyclerView, view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}