package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitnesstracker.databinding.ActivityExerciseDetailsBinding

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // تفعيل ViewBinding
        binding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // استقبال البيانات من Intent
        val name = intent.getStringExtra("name") ?: "No Name Available"
        val target = intent.getStringExtra("target") ?: "Not specified"
        val gifUrl = intent.getStringExtra("gifUrl") ?: "default_image_url"
        val secondaryMuscles = intent.getStringExtra("secondaryMuscles") ?: "Not specified"
        val bodyPart = intent.getStringExtra("bodyPart") ?: "Not specified"
        val instructions = intent.getStringArrayListExtra("instructions") ?: listOf()

        // تعيين القيم إلى الـ Views باستخدام View Binding
        binding.exerciseName.text = name
        binding.exerciseTarget.text = "Target :  $target"
        binding.secondryMuscles.text = "Secondary Muscles :  $secondaryMuscles"
        binding.bodyPart.text = "Body Part :  $bodyPart"
        binding.exerciseInstructions.text = instructions.joinToString(" \n ") { " *  $it" }

        // Loading the exercise image with Glide and handling null gifUrl
        Glide.with(this)
            .load(gifUrl)
            .placeholder(com.example.fitnesstracker.R.drawable.help)  // Fallback image if URL is invalid
            .into(binding.exerciseImage)
    }
}
