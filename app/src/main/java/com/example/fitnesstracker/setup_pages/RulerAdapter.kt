package com.example.fitnesstracker.setup_pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class RulerAdapter(
    private val range: IntRange,
    private val onWeightSelected: (Int) -> Unit
) : RecyclerView.Adapter<RulerAdapter.RulerViewHolder>() {

    inner class RulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tickText: TextView = itemView.findViewById(R.id.tickText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ruler, parent, false)
        return RulerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
        val weight = range.elementAt(position)
        holder.tickText.text = weight.toString()
        holder.itemView.setOnClickListener { onWeightSelected(weight) }
    }

    override fun getItemCount(): Int = range.count()
}
