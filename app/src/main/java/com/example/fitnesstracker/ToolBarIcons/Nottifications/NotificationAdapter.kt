package com.example.fitnesstracker.ToolBarIcons.Nottifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter : ListAdapter<NotificationEntity, NotificationAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(old: NotificationEntity, new: NotificationEntity) = old.id == new.id
        override fun areContentsTheSame(old: NotificationEntity, new: NotificationEntity) = old == new
    }
) {
    inner class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationEntity) {
            binding.tvTitle.text = item.title
            binding.tvMessage.text = item.message
            binding.tvDate.text = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}
