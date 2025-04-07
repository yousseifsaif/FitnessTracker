package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesstracker.R

class ExerciseApiAdapter(private var exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseApiAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseTarget: TextView = itemView.findViewById(R.id.exerciseTarget)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercises, parent, false)
        return ExerciseViewHolder(view)

    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.exerciseTarget.text = "Target: ${exercise.target}"


        Glide.with(holder.itemView.context)
            .load(exercise.gifUrl)
            .into(holder.exerciseImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ExerciseDetailsActivity::class.java)
            intent.putExtra("name", exercise.name)
            intent.putExtra("target", exercise.target)
            intent.putExtra("gifUrl", exercise.gifUrl)
            intent.putExtra("secondaryMuscles", exercise.secondaryMuscles.joinToString(", "))
            intent.putExtra("bodyPart", exercise.bodyPart)
            intent.putStringArrayListExtra("instructions", ArrayList(exercise.instructions))
            context.startActivity(intent)

        }
    }


    override fun getItemCount(): Int = exercises.size

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()

    }
}
