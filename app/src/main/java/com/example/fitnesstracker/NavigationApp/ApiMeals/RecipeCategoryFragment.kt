package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fitnesstracker.NavigationApp.ApiMeals.room.MealDatabaseInstance
import com.example.fitnesstracker.NavigationApp.ApiMeals.room.MealEntity
import com.example.fitnesstracker.databinding.FragmentRecipeCategoryBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.fitnesstracker.NavigationApp.ApiMeals.room.MealDao

class RecipeCategoryFragment : Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: FragmentRecipeCategoryBinding

    private var category: String? = null
    private val appId = "2f26d578"
    private val appKey = "5d5f2265ddc5695214f78d350a248e67"
    private lateinit var mealDao: MealDao

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): RecipeCategoryFragment {
            val fragment = RecipeCategoryFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeCategoryBinding.inflate(inflater, container, false)
        category = arguments?.getString(ARG_CATEGORY)
        mealDao = MealDatabaseInstance.getDatabase(requireContext()).mealDao()

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        category?.let { searchRecipes(it) }

        return binding.root
    }

    private fun searchRecipes(query: String) {
        val call = RetrofitInstance.calable.searchRecipes("public", query, appId, appKey)
        call.enqueue(object : Callback<EdamamResponse> {
            override fun onResponse(
                call: Call<EdamamResponse>,
                response: Response<EdamamResponse>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.hits
                    if (!recipes.isNullOrEmpty()) {
                        displayRecipes(recipes)
                        saveRecipesToRoom(recipes)
                    } else {
                        Toast.makeText(requireContext(), "No recipes found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EdamamResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Connection Error. Showing saved recipes.", Toast.LENGTH_SHORT).show()
                loadSavedRecipes()
            }
        })
    }

    private fun displayRecipes(recipes: List<Hit>) {
        recipeAdapter = RecipeAdapter(recipes)
        binding.recyclerView.adapter = recipeAdapter
    }

    private fun saveRecipesToRoom(recipes: List<Hit>) {
        val mealDao = MealDatabaseInstance.getDatabase(requireContext()).mealDao()
        lifecycleScope.launch {
            recipes.forEach { hit ->
                val meal = MealEntity(
                    label = hit.recipe.label,
                    image = hit.recipe.image,
                    calories = hit.recipe.calories,
                    description = hit.recipe.description
                )
                mealDao.insertMeal(meal)
            }
        }
    }

    private fun loadSavedRecipes() {
        val mealDao = MealDatabaseInstance.getDatabase(requireContext()).mealDao()
        lifecycleScope.launch {
            val savedMeals = mealDao.getAllMealsOnce()
            val hits = savedMeals.map { meal ->
                Hit(
                    recipe = Recipe(
                        label = meal.label,
                        image = meal.image,
                        calories = meal.calories,
                        description = meal.description,
                        url = ""
                    )
                )
            }
            displayRecipes(hits)
        }
    }

}
