package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.databinding.ActivityBoarding2Binding

class Boarding2 : AppCompatActivity() {

    private lateinit var binding: ActivityBoarding2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        binding = ActivityBoarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skipButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        binding.btn1.setOnClickListener {
            val intent = Intent(this, Boarding3::class.java)
            startActivity(intent)
        }
    }
}
