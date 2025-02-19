package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityGenderBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Gender : AppCompatActivity() {
    val db = Firebase.firestore
    private var selectedGender: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = binding.button
        val maleSelection = binding.male
        val femaleSelection = binding.female


        maleSelection.setOnClickListener {
            selectedGender = "male"
            maleSelection.setBackgroundResource(R.drawable.gender_selector)
            femaleSelection.setBackgroundResource(R.drawable.male_bacground)
        }
        femaleSelection.setOnClickListener {
            selectedGender = "female"
            femaleSelection.setBackgroundResource(R.drawable.gender_selector)
            maleSelection.setBackgroundResource(R.drawable.male_bacground)
        }



        button.setOnClickListener {
            if (selectedGender == null) {
                Toast.makeText(this, "please select your gender...", Toast.LENGTH_SHORT).show();
            } else {
                val id = intent.getStringExtra("id")
                if (!id.isNullOrEmpty()) {
                    val editor = SharedPrefHelper(this).prefs.edit()
                    editor.putString("gender", selectedGender).apply()
                    updateUserField("gender", selectedGender!!, id)
                }
                val Nextintent = Intent(this, AgeActivity::class.java)
                Nextintent.putExtra("id", id)
                startActivity(Nextintent)
            }

        }

    }
}

