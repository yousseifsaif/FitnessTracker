package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class CategoryAdapter(
    private val categories: List<String>,
    private val listener: OnCategoryClickListener,
    private var selectedCategory: String? = null

) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: String)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCategory: TextView = itemView.findViewById(R.id.textViewCategory)

        fun bind(category: String) {
            textViewCategory.text = category

            if (category == selectedCategory) {
                textViewCategory.setBackgroundResource(R.drawable.category_bg_selected)
            } else {
                textViewCategory.setBackgroundResource(R.drawable.category_bg)
            }

            itemView.setOnClickListener {
                selectedCategory = category
                notifyDataSetChanged()
                listener.onCategoryClick(category)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}
