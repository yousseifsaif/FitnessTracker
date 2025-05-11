package com.example.fitnesstracker.setup_pages

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityDataUserBinding
import com.example.fitnesstracker.toast.updateOrientationLock
import com.google.android.material.bottomsheet.BottomSheetDialog

class DataUser : AppCompatActivity() {
    private lateinit var binding: ActivityDataUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGender.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, arrayOf("Male", "Female")
            )
        }
        binding.tvAge.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, (18..80).map { it.toString() }.toTypedArray()
            )
        }
        binding.tvHeight.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, (140..220).map { "$it cm" }.toTypedArray()
            )
        }
        binding.tvWeight.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, (40..150).map { "$it kg" }.toTypedArray()
            )
        }
        binding.tvActivity.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, arrayOf(
                    "Sedentary",
                    "Lightly Active",
                    "Moderately Active",
                    "Very Active",
                    "Super Active"
                )
            )
        }
        binding.tvGoal.setOnClickListener {
            showBottomSheetDialog(
                it as TextView, arrayOf("Lose Weight", "Maintain Weight", "Gain Muscle")
            )
        }

        binding.btnConfirm.setOnClickListener {
            val summary = """
                Gender: ${binding.tvGender.text}
                Age: ${binding.tvAge.text}
                Height: ${binding.tvHeight.text}
                Weight: ${binding.tvWeight.text}
                Activity: ${binding.tvActivity.text}
                Goal: ${binding.tvGoal.text}
            """.trimIndent()
            Toast.makeText(this, summary, Toast.LENGTH_LONG).show()
        }
    }

    private fun showBottomSheetDialog(targetTextView: TextView, options: Array<String>) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null)
        val container = view.findViewById<android.widget.LinearLayout>(R.id.optionContainer)

        options.forEach { option ->
            val optionView = LayoutInflater.from(this)
                .inflate(R.layout.item_option, container, false) as TextView
            optionView.text = option
            optionView.setOnClickListener {
                targetTextView.text = option
                dialog.dismiss()
            }
            container.addView(optionView)
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}