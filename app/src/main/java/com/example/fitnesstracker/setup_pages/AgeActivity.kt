package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R

class AgeActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       val userData=UserData()
        setContentView(R.layout.activity_age)
        val ageDisplay = findViewById<TextView>(R.id.ageDisplay)
        val ageSeekBar = findViewById<SeekBar>(R.id.ageSeekBar)
        val continueButton = findViewById<Button>(R.id.button)
        ageSeekBar.min=8
        ageSeekBar.max = 100 // Maximum age
        ageSeekBar.progress = 28 // Default age
        ageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ageDisplay.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

continueButton.setOnClickListener {intent= Intent(this,HeightActivitySelection::class.java)
startActivity(intent)
}


        }
}