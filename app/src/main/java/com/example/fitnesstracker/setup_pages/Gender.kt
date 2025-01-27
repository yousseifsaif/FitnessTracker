package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class Gender : AppCompatActivity() {
    private var selectedGender: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val database:FirebaseDatabase=FirebaseDatabase.getInstance()
        var myRef:DatabaseReference = database.getReference()
        val button = findViewById<Button>(R.id.button)
        val maleSelection = findViewById<ImageView>(R.id.male)
        val femaleSelection = findViewById<ImageView>(R.id.female)


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
                myRef.push().child("gender").setValue(selectedGender)
                intent = Intent(this, AgeActivity::class.java)
                startActivity(intent)
            }

            }

        }
    }

