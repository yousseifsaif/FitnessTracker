package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fitnesstracker.R

class MealDetailFragment : Fragment() {
    companion object {
        private const val MEAL_NAME = "meal_name"
        private const val MEAL_IMAGE = "meal_image"

        fun newInstance(mealName: String, mealImage: String): MealDetailFragment {
            val fragment = MealDetailFragment()
            val args = Bundle()
            args.putString(MEAL_NAME, mealName)
            args.putString(MEAL_IMAGE, mealImage)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_meal_detail, container, false)

        val mealName = arguments?.getString(MEAL_NAME)
        val mealImage = arguments?.getString(MEAL_IMAGE)

        val mealImageView: ImageView = view.findViewById(R.id.meal_detail_image)
        val mealNameTextView: TextView = view.findViewById(R.id.meal_detail_name)

        Glide.with(this).load(mealImage).into(mealImageView)

        mealNameTextView.text = mealName ?: "Unknown Meal"

        return view
    }
}
