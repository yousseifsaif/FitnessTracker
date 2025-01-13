package com.example.fitnesstracker.Boarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R

class Boarding4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_boarding4)
        val btn=findViewById<Button>(R.id.btn3)
        btn.setOnClickListener {
            val intent = Intent(this, Log_in::class.java)
            startActivity(intent)

        }


    }
}