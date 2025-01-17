package com.example.fitnesstracker.setup_pages

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R

class Gender : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.button)
        val maleSelection = findViewById<ImageView>(R.id.male)
        val femaleSelection = findViewById<ImageView>(R.id.female)
        val options = listOf(maleSelection, femaleSelection)
        options.forEach { ImageView ->
            ImageView.setOnClickListener {

                options.forEach { it.setBackgroundResource(R.drawable.male_bacground) }
                it.setBackgroundResource(R.drawable.gender_selector)
            }
        }
    }
}