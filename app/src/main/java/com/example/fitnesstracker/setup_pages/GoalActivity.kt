package com.example.fitnesstracker.setup_pages

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityGoalBinding
import com.google.firebase.firestore.FirebaseFirestore

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

                    val editor = SharedPrefHelper(this).prefs.edit()
                    editor.putString("selectedGoal",result).apply()
                    startActivity(
                        nav(
                            NavData(
                                ActivityLevel::class.java,
                                this,
                                id.toString()
                            )
                        )
                    )

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
