package com.example.fitnesstracker.NavigationApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class ExerciseAdapter(
    private val exercises: MutableList<String>,
    private val day: String,
    private val onDeleteExercise: (String, String) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        val btnDeleteExercise: ImageView = itemView.findViewById(R.id.btnDeleteExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.tvExerciseName.text = exercise

        holder.btnDeleteExercise.setOnClickListener {
            onDeleteExercise(day, exercise)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, exercises.size)
        }
    }

    override fun getItemCount() = exercises.size
}
