package com.example.fitnesstracker.NavigationApp.home

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.NavigationApp.ExerciseAdapter
import com.example.fitnesstracker.R


class WorkoutAdapter(
    private val days: MutableList<String>,
    private val exercisesMap: MutableMap<String, MutableList<String>>,
    private val onDeleteDay: (String) -> Unit,
    private val onDeleteExercise: (String, String) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayName: TextView = view.findViewById(R.id.tvDayName)
        val btnDeleteDay: ImageView = view.findViewById(R.id.btnDeleteDay)
        val rvExercises: RecyclerView = view.findViewById(R.id.rvExercises)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.tvDayName.text = day
        holder.tvDayName.setTextColor(Color.WHITE)
        holder.tvDayName.setTypeface(null, Typeface.BOLD)
        holder.tvDayName.textSize = 22f
        holder.rvExercises.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context)
            adapter = ExerciseAdapter(exercisesMap[day] ?: mutableListOf(), day, onDeleteExercise)
        }

        holder.btnDeleteDay.setOnClickListener {
            onDeleteDay(day)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, days.size)
        }
    }

    override fun getItemCount() = days.size
}
