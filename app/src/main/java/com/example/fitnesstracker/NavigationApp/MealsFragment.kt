package com.example.fitnesstracker.NavigationApp

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.NavigationApp.ApiMeals.*
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.FragmentMealsBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MealsFragment : Fragment() {
    private var _binding: FragmentMealsBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiMealsAdapter: ApiMealsAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val mealsList = mutableListOf<ApiMeals>()
    private val categories = listOf("Breakfast", "Seafood", "Vegetarian", "Dessert", "Chicken", "Beef", "Pasta", "Side", "Starter", "Vegan", "Miscellaneous", "Goat", "Lamb", "Pork", "Side")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // إعداد RecyclerView للوجبات
        apiMealsAdapter = ApiMealsAdapter(mealsList) { meal ->
            Log.d("MEAL_CLICK", "Meal Clicked: ${meal.strMeal}")

            val fragment = MealDetailFragment.newInstance(meal.strMeal, meal.strMealThumb)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = apiMealsAdapter

        categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.OnCategoryClickListener {
            override fun onCategoryClick(category: String) {
                getMeals(category) // تحديث الوجبات عند اختيار تصنيف جديد
            }
        })

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = categoryAdapter
        binding.categoryRecyclerView.addItemDecoration(CategoryItemDecoration(25))

        // جلب بيانات أول تصنيف
        getMeals("Breakfast")
    }

    private fun getMeals(category: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CallableApi::class.java)

        Log.d("API_REQUEST", "Fetching meals for category: $category")

        api.getCategory(category).enqueue(object : Callback<ApiMealsResponse> {
            override fun onResponse(call: Call<ApiMealsResponse>, response: Response<ApiMealsResponse>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        mealsList.clear()
                        mealsList.addAll(meals)
                        apiMealsAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("API_RESPONSE", "No meals found!")
                    }
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiMealsResponse>, t: Throwable) {
                Log.e("API_RESPONSE", "Failure: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CategoryItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = space
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = space
            }
        }
    }
}
