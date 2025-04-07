package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesstracker.R

class ApiMealsAdapter(
    private val mealsList: List<ApiMeals>,
    private val onClick: (ApiMeals) -> Unit
) : RecyclerView.Adapter<ApiMealsAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealsList[position]
        holder.bind(meal)
        holder.itemView.setOnClickListener { onClick(meal) } // ✅ تمرير الكائن كله
    }

    override fun getItemCount(): Int = mealsList.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealNameTextView: TextView = itemView.findViewById(R.id.meal_name)
        private val mealImageView: ImageView = itemView.findViewById(R.id.meal_image)

        fun bind(meal: ApiMeals) {
            mealNameTextView.text = meal.strMeal
            Glide.with(itemView.context).load(meal.strMealThumb).into(mealImageView)
        }
    }
}
