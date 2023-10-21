package com.thomasruegg.dayournal.ui.entries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thomasruegg.dayournal.R
import com.thomasruegg.dayournal.model.JournalEntry
import com.thomasruegg.dayournal.ui.entries.JournalEntryListAdapter.JournalEntryViewHolder

class JournalEntryListAdapter : ListAdapter<JournalEntry, JournalEntryViewHolder>(JournalEntryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalEntryViewHolder {
        return JournalEntryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: JournalEntryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.content)
    }

    class JournalEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val journalEntryItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            journalEntryItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): JournalEntryViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return JournalEntryViewHolder(view)
            }
        }
    }

    class JournalEntryComparator : DiffUtil.ItemCallback<JournalEntry>() {
        override fun areItemsTheSame(oldItem: JournalEntry, newItem: JournalEntry): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: JournalEntry, newItem: JournalEntry): Boolean {
            return oldItem.content == newItem.content
        }
    }
}