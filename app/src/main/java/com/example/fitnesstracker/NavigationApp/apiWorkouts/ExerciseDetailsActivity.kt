package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityExerciseDetailsBinding

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val exercise = intent.getParcelableExtra<ExerciseEntity>("exercises")
//
//        if (exercise != null) {
//            bindExerciseData(exercise)
//        } else {
//            bindIndividualData()
//        }
//
//
//        exercise?.let {
//            bindExerciseData(it)
//        } ?: run {
//            Toast.makeText(this, "Exercise data not found", Toast.LENGTH_SHORT).show()
//            finish()
//        }


        val name = intent.getStringExtra("name") ?: "No Name Available"
        val target = intent.getStringExtra("target") ?: "Not specified"
        val gifUrl = intent.getStringExtra("gifUrl") ?: "default_image_url"
        val secondaryMuscles = intent.getStringExtra("secondaryMuscles") ?: "Not specified"
        val bodyPart = intent.getStringExtra("bodyPart") ?: "Not specified"
        val instructions = intent.getStringArrayListExtra("instructions") ?: listOf()

        binding.exerciseName.text = name
        binding.exerciseTarget.text = "Target :  $target"
        binding.secondryMuscles.text = "Secondary Muscles :  $secondaryMuscles"
        binding.bodyPart.text = "Body Part :  $bodyPart"
        binding.exerciseInstructions.text = instructions.joinToString(" \n ") { " *  $it" }

        Glide.with(this)
            .load(gifUrl)
            .placeholder(com.example.fitnesstracker.R.drawable.help)
            .into(binding.exerciseImage)
    }

    private fun bindExerciseData(exercise: ExerciseEntity) {
        binding.apply {
            exerciseName.text = exercise.name
            exerciseTarget.text = "Target: ${exercise.target}"
            secondryMuscles.text = "Secondary Muscles: ${exercise.secondaryMuscles}"
            bodyPart.text = "Body Part: ${exercise.bodyPart}"

            val instructionsList = exercise.instructions.split("\n")
            exerciseInstructions.text = instructionsList.joinToString("\n") { "• $it" }

            Glide.with(this@ExerciseDetailsActivity)
                .load(exercise.gifUrl)
                .placeholder(R.drawable.help)
                .into(exerciseImage)
        }
    }

    private fun bindIndividualData() {
        binding.apply {
            exerciseName.text = intent.getStringExtra("name") ?: "No Name Available"
            exerciseTarget.text = "Target: ${intent.getStringExtra("target") ?: "Not specified"}"
            secondryMuscles.text =
                "Secondary Muscles: ${intent.getStringExtra("secondaryMuscles") ?: "Not specified"}"
            bodyPart.text = "Body Part: ${intent.getStringExtra("bodyPart") ?: "Not specified"}"
            exerciseInstructions.text =
                "Instructions: ${intent.getStringExtra("instructions") ?: "Not specified"}"


            val instructions = intent.getStringArrayListExtra("instructions") ?: listOf()
            exerciseInstructions.text = instructions.joinToString("\n") { "• $it" }

            Glide.with(this@ExerciseDetailsActivity)
                .load(intent.getStringExtra("gifUrl"))
                .placeholder(R.drawable.help)
                .into(exerciseImage)
        }

//        if (intent.getParcelableExtra<ExerciseEntity>("exercises") == null) {
//            Toast.makeText(this, "Exercise data incomplete", Toast.LENGTH_SHORT).show()
//        }
    }
}