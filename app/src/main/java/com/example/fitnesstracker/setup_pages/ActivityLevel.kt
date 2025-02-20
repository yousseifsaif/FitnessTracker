package com.example.fitnesstracker.setup_pages

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.databinding.ActivityLevelBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

class ActivityLevel : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        val beginnerBtn = binding.beginnerBtn
        val intermediateBtn = binding.IntermediateBtn
        val advanceBtn = binding.AdvanceBtn

        val buttons = listOf(beginnerBtn, intermediateBtn, advanceBtn)
        var ActivityLevel = ""

        beginnerBtn.setOnClickListener {
            updateButtons(buttons, beginnerBtn)
            ActivityLevel = "beginner"
        }
        intermediateBtn.setOnClickListener {
            updateButtons(buttons, intermediateBtn)
            ActivityLevel = "intermediate"
        }
        advanceBtn.setOnClickListener {
            updateButtons(buttons, advanceBtn)
            ActivityLevel = "advance"
        }

        binding.continueButton.setOnClickListener {
            val id = intent.getStringExtra("id")
            if (id == null) {
                Log.e("ActivityLevel", "Error: User ID is null")
            }


            Log.d("ActivityLevel", "-1")

            if (id != null) {
                updateUserField("ActivityLevel", ActivityLevel, id)

                db.collection("users").document(id).get()
                    .addOnSuccessListener { document ->
                        Log.d("ActivityLevel", "1")
                        val data = document.toObject(SharedPrefHelper.User::class.java) ?: SharedPrefHelper.User()
                        Log.d("ActivityLevel", "2")
                        val calories = calculateCal(data).roundToInt()
                        updateUserField("calories", calories, id)
                        Log.d("ActivityLevel", "3")

                        val editor = SharedPrefHelper(this).prefs.edit()
                        editor.putString("ActivityLevel", ActivityLevel).apply()
                        editor.putInt("calories", calories).apply()

                        startActivity(
                            nav(
                                NavData(
                                    MainActivity::class.java,
                                    this,
                                    id
                                )
                            )
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ActivityLevel", "Error fetching user data: ${exception.message}")
                    }
            }
        }
    }
}
