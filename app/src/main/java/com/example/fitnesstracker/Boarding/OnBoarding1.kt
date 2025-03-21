package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.getNextActivity
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.setup_pages.saveLoginState

class OnBoarding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding_1)








        Handler().postDelayed({

            val sharedPreferences: SharedPreferences =
                getSharedPreferences("AppPrefs", MODE_PRIVATE)

            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val sharedPrefHelper = SharedPrefHelper(this)
            Log.d("OnBoarding1", "SharedPrefHelper initialized")

            val userData = sharedPrefHelper.getUserFromPrefs()
            Log.d("OnBoarding1", "UserData retrieved: $userData")

            // Redirect user
            if (isLoggedIn) {
                Log.d("OnBoarding1", (userData).toString())
                startActivity(
                    nav(
                        NavData(
                            getNextActivity(userData),
                            this,
                            userData.id
                        )
                    )
                )            } else {
                startActivity(Intent(this, Boarding2::class.java))
            }

            finish()
        }, 3000)

    }
}