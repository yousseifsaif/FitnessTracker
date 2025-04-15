package com.example.fitnesstracker.NavigationApp.ApiMeals

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecipeCategoryPagerAdapter(fragment: Fragment, private val categories: List<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return RecipeCategoryFragment.newInstance(categories[position])
    }
}