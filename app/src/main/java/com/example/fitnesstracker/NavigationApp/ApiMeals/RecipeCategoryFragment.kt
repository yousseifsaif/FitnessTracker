package com.example.fitnesstracker.NavigationApp.ApiMeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fitnesstracker.databinding.FragmentRecipeCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeCategoryFragment : Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: FragmentRecipeCategoryBinding

    private var category: String? = null
    private val appId = "2f26d578"
    private val appKey = "5d5f2265ddc5695214f78d350a248e67"

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

        // إعداد الـ RecyclerView والـ Adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // استدعاء الوصفات عند تحميل الـ Fragment
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
                    } else {
                        Toast.makeText(requireContext(), "No recipes found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EdamamResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayRecipes(recipes: List<Hit>) {
        // تحديث الـ Adapter بالوصفات المسترجعة
        recipeAdapter = RecipeAdapter(recipes) // تأكد من استخدام القائمة المسترجعة من الـ API
        binding.recyclerView.adapter = recipeAdapter
    }
}
