package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesstracker.R

class ExerciseApiAdapter(
    private var exercises: List<Exercise>,
    private val onFavoriteClick: (Exercise) -> Unit,
    private val onItemClick: (Exercise) -> Unit,
    private val sharedPreferences: SharedPreferences? = null

) : RecyclerView.Adapter<ExerciseApiAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseTarget: TextView = itemView.findViewById(R.id.exerciseTarget)
        val favoriteButton: ImageView = itemView.findViewById(R.id.favoriteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercises, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {

        val exercise = exercises[position]

        val isFavorite = exercise.isFavorite || (sharedPreferences?.getBoolean(
            "fav_${exercise.id}", false
        ) == true)

        holder.exerciseName.text = exercise.name
        holder.exerciseTarget.text = "Target: ${exercise.target}"

        Glide.with(holder.itemView.context).load(exercise.gifUrl).into(holder.exerciseImage)
        val favoriteIcon = if (isFavorite) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.ic_favorite_border
        }
        holder.favoriteButton.setImageResource(favoriteIcon)

        updateFavoriteIcon(holder.favoriteButton, exercise.isFavorite)

        holder.favoriteButton.setOnClickListener {
            onFavoriteClick(exercise)
            exercise.isFavorite = !exercise.isFavorite
            updateFavoriteIcon(holder.favoriteButton, exercise.isFavorite)
            notifyItemChanged(position)

        }

        holder.itemView.setOnClickListener {
            onItemClick(exercise)
        }
    }

    private fun updateFavoriteIcon(button: ImageView, isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        button.setImageResource(icon)
    }

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = exercises.size
}