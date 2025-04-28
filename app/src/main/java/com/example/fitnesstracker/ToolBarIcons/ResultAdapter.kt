package com.example.fitnesstracker.ToolBarIcons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ItemResultBinding

class ResultAdapter(
    private val onExerciseClick: (ExerciseEntity) -> Unit
) : ListAdapter<ExerciseEntity, ResultAdapter.ResultViewHolder>(DiffCallback()) {

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExerciseEntity) {
            binding.apply {
                tvResultName.text = item.name
                tvResultBodyPart.text = item.bodyPart

                root.setOnClickListener {
                    onExerciseClick(item)
                }

                // لتأثيرات النقر
                root.background = ContextCompat.getDrawable(
                    root.context,
                    R.drawable.selectable_item_background
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ExerciseEntity>() {
        override fun areItemsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity) =
            oldItem == newItem
    }
}