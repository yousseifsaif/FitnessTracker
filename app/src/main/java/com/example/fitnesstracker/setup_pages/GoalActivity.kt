package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.NavigationApp.HomeActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityGoalBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

class GoalActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = FirebaseFirestore.getInstance()
        val binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radioGroup = binding.radioGroup
        val continueButton = binding.continueButton
        progressBar = binding.progressBar

        continueButton.setOnClickListener {
            if (radioGroup.checkedRadioButtonId != -1) {
                val result = when (radioGroup.checkedRadioButtonId) {
                    R.id.lose -> "lose"
                    R.id.gain -> "gain"
                    R.id.massGain -> "massGain"
                    R.id.shapeBody, R.id.others -> "shapeBody"
                    else -> "def"
                }

                val id = intent.getStringExtra("id")
                if (id != null) {
                    updateUserField("selectedGoal", result, id)

                    showLoading()

                    db.collection("users").document(id).get()
                        .addOnSuccessListener { document ->
                            val data = document.toObject(UserData::class.java) ?: UserData()

                            updateUserField("calories", calculateCal(data).roundToInt(), id)

                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("id", data.id)

                            startActivity(intent)
                            hideLoading()
                        }
                        .addOnFailureListener {
                            hideLoading()
                        }
                }
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }
}
