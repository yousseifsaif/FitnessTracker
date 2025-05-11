package com.example.fitnesstracker.setup_pages

import ButtonClickUtil
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityGenderBinding
import com.example.fitnesstracker.toast.updateOrientationLock
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
            ButtonClickUtil.preventSpamClick(this) {

                if (selectedGender == null) {
                    Toast.makeText(this, "please select your gender...", Toast.LENGTH_SHORT).show()
                } else {
                    val id = intent.getStringExtra("id")
                    if (!id.isNullOrEmpty()) {
                        SharedPrefHelper(this).prefs.edit { putString("gender", selectedGender) }
                        updateUserField("gender", selectedGender!!, id)
                        startActivity(nav(NavData(AgeActivity::class.java, this, id.toString())))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}