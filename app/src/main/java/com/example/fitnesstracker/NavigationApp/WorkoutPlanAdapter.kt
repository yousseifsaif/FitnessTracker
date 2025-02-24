package com.example.fitnesstracker.NavigationApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class WorkoutPlanAdapter(
    private val plans: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<WorkoutPlanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planName: TextView = view.findViewById(R.id.tvWorkoutPlanName)
        val btnSelect: Button = view.findViewById(R.id.btnSelectPlan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = plans[position]
        holder.planName.text = plan
        holder.btnSelect.setOnClickListener { onClick(plan) }
    }

    override fun getItemCount() = plans.size
}
