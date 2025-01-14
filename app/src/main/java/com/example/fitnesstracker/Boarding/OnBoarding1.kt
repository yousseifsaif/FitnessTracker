package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R

class OnBoarding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding_1)
        Handler().postDelayed({
            val intent = Intent(this, Boarding2::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}