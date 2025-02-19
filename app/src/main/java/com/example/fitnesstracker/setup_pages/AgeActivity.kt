package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivityAgeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AgeActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ageDisplay = binding.ageDisplay
        val ageSeekBar = binding.ageSeekBar
        val continueButton = binding.button

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ageSeekBar.min = 8
        }
        ageSeekBar.max = 100
        ageSeekBar.progress = 18

        binding.ageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ageDisplay.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        continueButton.setOnClickListener {
            val id = intent.getStringExtra("id")

            if (!id.isNullOrEmpty()) {
                val userAge = ageDisplay.text.toString().toInt()
                updateUserField("age", userAge, id)
                val editor = SharedPrefHelper(this).prefs.edit()
                editor.putString("age", userAge.toString()).apply()
            }


            val nextIntent = Intent(this, HeightActivitySelection::class.java)
            nextIntent.putExtra("id", id)
            startActivity(nextIntent)
        }
    }
}
