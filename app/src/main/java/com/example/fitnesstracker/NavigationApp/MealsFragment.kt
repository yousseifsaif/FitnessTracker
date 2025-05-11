package com.example.fitnesstracker.NavigationApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.NavigationApp.ApiMeals.RecipeCategoryPagerAdapter
import com.example.fitnesstracker.databinding.FragmentMealsBinding
import com.google.android.material.tabs.TabLayoutMediator

class MealsFragment : Fragment() {

    private lateinit var binding: FragmentMealsBinding

    // Edamam credentials
    private val appId = "2f26d578"
    private val appKey = "5d5f2265ddc5695214f78d350a248e67"
    private val categories = listOf(
        "Chicken",
        "Beef",
        "Vegetarian",
        "Pasta",
        "Salad",
        "Seafood",
        "Soup",
        "Dessert",
        "Vegan",
        "Snacks",
        "Breakfast",
        "Drinks",
        "Healthy",
        "Gluten-Free",
        "Low-Carb",


        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)

        // عرض الفئات عند تحميل الـ Fragment
        val adapter = RecipeCategoryPagerAdapter(this, categories)
        binding.viewPager.adapter = adapter

        // ربط التابات مع ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()

        return binding.root
    }
}