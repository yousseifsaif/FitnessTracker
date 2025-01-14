package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R

class Boarding3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_boarding3)
        var btn = findViewById<Button>(R.id.btn3)
        btn.setOnClickListener {
            val intent = Intent(this, Boarding4::class.java)


            startActivity(intent)
        }
    }
}