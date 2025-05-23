package com.example.fitnesstracker.Boarding

import ButtonClickUtil
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.R


class Boarding4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_boarding4)
        val btn = findViewById<Button>(R.id.btn3)
        btn.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}