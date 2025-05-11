package com.example.fitnesstracker.setup_pages

import ButtonClickUtil
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fitnesstracker.databinding.ActivityAgeBinding
import com.example.fitnesstracker.toast.updateOrientationLock

class AgeActivity : AppCompatActivity() {

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
            ButtonClickUtil.preventSpamClick(this) {
                val id = intent.getStringExtra("id")

                if (!id.isNullOrEmpty()) {
                    val userAge = ageDisplay.text.toString().toInt()
                    updateUserField("age", userAge, id)
                    SharedPrefHelper(this).prefs.edit {
                        putInt("age", userAge)
                    }
                }

                startActivity(
                    nav(
                        NavData(
                            HeightActivitySelection::class.java, this, id.toString()
                        )
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}