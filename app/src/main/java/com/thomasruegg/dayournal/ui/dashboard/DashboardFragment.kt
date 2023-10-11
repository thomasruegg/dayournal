package com.thomasruegg.dayournal.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.databinding.FragmentDashboardBinding
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar : CalendarView = view.findViewById(R.id.calendar)
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val todayButton : FloatingActionButton = view.findViewById(R.id.todayButton)
        todayButton.setOnClickListener {
            calendar.setDate(currentTimeInMillis, true, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}