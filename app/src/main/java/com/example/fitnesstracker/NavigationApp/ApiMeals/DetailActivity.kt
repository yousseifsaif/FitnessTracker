package com.example.fitnesstracker.NavigationApp.ApiMeals


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitnesstracker.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeName = intent.getStringExtra("RECIPE_NAME")
        val recipeCalories = intent.getIntExtra("RECIPE_CALORIES", 0)
        val recipeImage = intent.getStringExtra("RECIPE_IMAGE")
        val recipeDescription = intent.getStringExtra("RECIPE_DESCRIPTION")

        binding.recipeName.text = recipeName
        binding.recipeCalories.text = "Calories: $recipeCalories"
        Glide.with(this).load(recipeImage).into(binding.recipeImage)
        binding.recipeDescription.text = recipeDescription
    }
}