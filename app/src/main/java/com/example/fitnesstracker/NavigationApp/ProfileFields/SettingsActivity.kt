package com.example.fitnesstracker.NavigationApp.ProfileFields

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivitySettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.jvm.java

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.switchDarkMode.isChecked = isDarkModeEnabled(this)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkModeEnabled(this, isChecked)
            restartApp()
        }
        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgottenPassword::class.java))
        }

    }
    fun setDarkModeEnabled(context: Context, isEnabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("dark_mode", isEnabled).apply()

        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("dark_mode", false)
    }
    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}