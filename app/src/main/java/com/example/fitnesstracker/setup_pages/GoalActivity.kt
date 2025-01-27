package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.NavigationApp.HomeActivity
import com.example.fitnesstracker.R

class GoalActivity : AppCompatActivity()  {
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goal)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val continueButton = findViewById<Button>(R.id.continueButton)
        progressBar=findViewById(R.id.progressBar)
        continueButton.setOnClickListener {
            if (radioGroup.checkedRadioButtonId != -1) {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.lose -> {
//                    var loseWeight=(10*WeightActivitySelection.weight+6.25*HeightActivitySelection.height-5*AgeActivity.age+5)
                        showLoading()
                        simulateLoadingProcess()
                        intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.gain -> {
                        showLoading()
                        simulateLoadingProcess()
                        intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.massGain -> {
                        showLoading()
                        simulateLoadingProcess()
                        intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.shapeBody -> {
                        showLoading()
                        simulateLoadingProcess()
                        intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.others -> {
                        showLoading()
                        simulateLoadingProcess()
                        intent= Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
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
    private fun simulateLoadingProcess() {
        // Simulate a background task with a delay
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoading()
            // Perform any action after loading is complete
        }, 3000) // 3 seconds delay

}
}
