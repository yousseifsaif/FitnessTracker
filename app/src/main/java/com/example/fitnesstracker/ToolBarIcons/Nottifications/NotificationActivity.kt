package com.example.fitnesstracker.ToolBarIcons.Nottifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.apiWorkouts.AppDatabase
import com.example.fitnesstracker.databinding.FragmentNotificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationAdapter = NotificationAdapter()
        binding.recyclerView.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }

        val dao = AppDatabase.getInstance(this).notificationDao()
        dao.getAllNotifications().observe(this) { notifications ->
            notificationAdapter.submitList(notifications)
            Log.d("NotificationActivity", "Loaded notifications: ${notifications.size}")
        }

        binding.btnClearAll.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dao.clearAll()
                Log.d("NotificationActivity", "All notifications cleared")
            }
        }
    }}

