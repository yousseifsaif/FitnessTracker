package com.example.fitnesstracker.ToolBarIcons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ItemRecentSearchBinding

class RecentSearchAdapter(
    private val onItemClick: (String) -> Unit,
    private val onItemLongClick: (RecentSearch) -> Unit

) : ListAdapter<RecentSearch, RecentSearchAdapter.RecentSearchViewHolder>(DiffCallback()) {

    inner class RecentSearchViewHolder(private val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecentSearch) {
            binding.apply {
                tvSearchQuery.text = item.query

                root.setOnClickListener {
                    onItemClick(item.query)
                }

                root.setOnLongClickListener {
                    onItemLongClick(item)
                    true
                }

                // لتأثيرات النقر
                root.background = ContextCompat.getDrawable(
                    root.context,
                    R.drawable.selectable_item_background
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val binding = ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<RecentSearch>() {
        override fun areItemsTheSame(oldItem: RecentSearch, newItem: RecentSearch) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RecentSearch, newItem: RecentSearch) =
            oldItem == newItem
    }
}