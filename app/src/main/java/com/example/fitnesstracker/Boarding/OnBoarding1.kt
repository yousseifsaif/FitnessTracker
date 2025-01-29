package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.NavigationApp.HomeActivity
import com.example.fitnesstracker.R

class OnBoarding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding_1)
        Handler().postDelayed({

            val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            // Redirect user
            if (isLoggedIn) {
                startActivity(Intent(this, HomeActivity::class.java)
                ) // Go to Home Page
            } else {
                startActivity(Intent(this, Boarding2::class.java)) // Go to Boarding Page
            }

            finish()
        }, 3000)

    }
}