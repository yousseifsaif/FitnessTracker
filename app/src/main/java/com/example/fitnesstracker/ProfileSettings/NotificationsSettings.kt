package com.example.fitnesstracker.ProfileSettings

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cancelAllNotifications
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityNotificationsSettingsBinding
import com.example.fitnesstracker.toast.updateOrientationLock

class NotificationsSettings : AppCompatActivity() {

    private lateinit var settings: NotificationSettings

    @RequiresApi(Build.VERSION_CODES.O)
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

        settings = NotificationSettings.load(this)

        binding.notificationSwitch.isChecked = settings.generalNotification
        binding.soundSwitch.isChecked = settings.sound
        binding.doNotDisturbModeSwitch.isChecked = settings.dontDisturbMode
        binding.vibrateSwitch.isChecked = settings.vibrate
        binding.lockScreenSwitch.isChecked = settings.lockScreen
        binding.remindersSwitch.isChecked = settings.reminders

        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION") getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        binding.backButton.setOnClickListener { finish() }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->

            settings.generalNotification = isChecked
            NotificationSettings.save(this, settings)
            cancelAllNotifications(this)
        }

        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            settings.sound = isChecked
            NotificationSettings.save(this, settings)
        }

        binding.doNotDisturbModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settings.dontDisturbMode = isChecked
            NotificationSettings.save(this, settings)
        }

        binding.vibrateSwitch.setOnCheckedChangeListener { _, isChecked ->
            settings.vibrate = isChecked
            NotificationSettings.save(this, settings)
            if (isChecked) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator.cancel()
            }
        }

        binding.lockScreenSwitch.setOnCheckedChangeListener { _, isChecked ->
            settings.lockScreen = isChecked
            NotificationSettings.save(this, settings)
            requestedOrientation = if (isChecked) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        binding.remindersSwitch.setOnCheckedChangeListener { _, isChecked ->
            settings.reminders = isChecked
            NotificationSettings.save(this, settings)
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}