package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesstracker.databinding.ItemRecipeBinding

class RecipeAdapter(private val recipes: List<Hit>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.binding.recipeName.text = recipe.recipe.label
        holder.binding.recipeCalories.text = "Calories: ${recipe.recipe.calories.toInt()}"

        Glide.with(holder.itemView.context).load(recipe.recipe.image)
            .into(holder.binding.recipeImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("RECIPE_NAME", recipe.recipe.label)
                putExtra("RECIPE_CALORIES", recipe.recipe.calories.toInt())
                putExtra("RECIPE_IMAGE", recipe.recipe.image)
                putExtra(
                    "RECIPE_DESCRIPTION",
                    recipe.recipe.description ?: "No description available"
                )
                putExtra("RECIPE_URL", recipe.recipe.url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipes.size
}