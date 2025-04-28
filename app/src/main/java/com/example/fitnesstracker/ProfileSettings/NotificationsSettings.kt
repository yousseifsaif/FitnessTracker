package com.example.fitnesstracker.ProfileSettings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityNotificationsSettingsBinding

class NotificationsSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNotificationsSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val isGeneralNotification = sharedPreferences.getBoolean("generalNotification", false)
        val isSound = sharedPreferences.getBoolean("Sound", false)
        val isDontDisturbMode = sharedPreferences.getBoolean("dontDisturbMode", false)
        val isVibrate = sharedPreferences.getBoolean("Vibrate", false)
        val isLockScreen = sharedPreferences.getBoolean("LockScreen", false)
        val isReminders = sharedPreferences.getBoolean("Reminders", false)

        binding.notificationSwitch.isChecked = isGeneralNotification
        binding.soundSwitch.isChecked = isSound
        binding.doNotDisturbModeSwitch.isChecked = isDontDisturbMode
        binding.vibrateSwitch.isChecked = isVibrate
        binding.lockScreenSwitch.isChecked = isLockScreen
        binding.remindersSwitch.isChecked = isReminders

        // Update preferences when switches are toggled
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("generalNotification", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable general notifications
            } else {
                // Disable general notifications
            }
        }

        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("Sound", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable sound
            } else {
                // Disable sound
            }
        }

        binding.doNotDisturbModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("dontDisturbMode", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable Do Not Disturb mode
            } else {
                // Disable Do Not Disturb mode
            }
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("Vibrate", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable vibrate mode
            } else {
                // Disable vibrate mode
            }
        }

        binding.lockScreenSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("LockScreen", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable lock screen notifications
            } else {
                // Disable lock screen notifications
            }
        }

        binding.remindersSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("Reminders", isChecked)
            editor.apply()
            if (isChecked) {
                // Enable reminders
            } else {
                // Disable reminders
            }
        }
    }
}
