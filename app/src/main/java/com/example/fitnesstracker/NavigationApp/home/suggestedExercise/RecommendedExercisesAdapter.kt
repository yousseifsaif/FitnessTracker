package com.example.fitnesstracker.NavigationApp.home.suggestedExercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesstracker.NavigationApp.apiWorkouts.Exercise
import com.example.fitnesstracker.R

class RecommendedExercisesAdapter(
    private val exercises: List<Exercise>
) : RecyclerView.Adapter<RecommendedExercisesAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgExercise)
        val name: TextView = itemView.findViewById(R.id.tvExerciseName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_horizontal, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = exercises[position]
        holder.name.text = item.name
        Glide.with(holder.itemView.context)
            .load(item.gifUrl)
            .into(holder.img)

        holder.itemView.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(300)
            .withEndAction {
                holder.itemView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }


    override fun getItemCount(): Int = exercises.size
}