package com.example.fitnesstracker.NavigationApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ApiCallable
import com.example.fitnesstracker.NavigationApp.apiWorkouts.CategoryAdapter
import com.example.fitnesstracker.NavigationApp.apiWorkouts.CategoryData
import com.example.fitnesstracker.NavigationApp.apiWorkouts.CategoryWorkoutsActivity
import com.example.fitnesstracker.NavigationApp.apiWorkouts.Exercise
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseApiAdapter
import com.example.fitnesstracker.databinding.FragmentWorkoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class WorkoutsFragment : Fragment() {

    private lateinit var exerciseAdapter: ExerciseApiAdapter
    private lateinit var binding: FragmentWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exerciseAdapter = ExerciseApiAdapter(ArrayList())
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
val ListOfExercises = listOf<CategoryData>(
    CategoryData("chest", com.example.fitnesstracker.R.drawable.chestworkouts),
    CategoryData("back", com.example.fitnesstracker.R.drawable.back),
    CategoryData("lower arms", com.example.fitnesstracker.R.drawable.lowerarms),
    CategoryData("lower legs", com.example.fitnesstracker.R.drawable.lowerrlegs),
    CategoryData("shoulders", com.example.fitnesstracker.R.drawable.shoulders),
    CategoryData("upper arms", com.example.fitnesstracker.R.drawable.upperarms),
    CategoryData("upper legs", com.example.fitnesstracker.R.drawable.upperlegs),
    CategoryData("waist", com.example.fitnesstracker.R.drawable.waist),
    CategoryData("neck", com.example.fitnesstracker.R.drawable.neck),
    CategoryData("cardio", com.example.fitnesstracker.R.drawable.cardio),

    )
        val categoryAdapter = CategoryAdapter(ListOfExercises) { selectedCategory ->
            val intent = Intent(requireContext(), CategoryWorkoutsActivity::class.java)
            intent.putExtra("BODY_PART", selectedCategory)
            startActivity(intent)
        }


        binding.recyclerViewCategory.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.recyclerViewCategory.adapter = categoryAdapter

        return binding.root
    }
//    private fun getExercises(bodyPart: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://exercisedb.p.rapidapi.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val callable = retrofit.create(ApiCallable::class.java)
//
//        // Assuming getExercise() returns a list of exercises
//        callable.getExercise(bodyPart).enqueue(object : Callback <List<Exercise>> {  // Use List<Exercise> here
//            override fun onResponse(
//                call: Call<List<Exercise>>,
//                response: Response<List<Exercise>>
//            ) {
//                val exercises = response.body()
//                if (exercises != null) {
//                    showExercise(ArrayList(exercises))  // Cast to ArrayList if needed
//                } else {
//                    Log.d("TAG", "onResponse: null")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
//                Log.e("WorkoutsFragment", "Error fetching exercises", t)  // Add throwable to log
//            }
//        })
//    }
//    private fun showExercise(exercise: ArrayList<Exercise>){
//        val adapter = ExerciseApiAdapter(exercise)
//        binding.recyclerView.adapter = adapter
//        exerciseAdapter.updateExercises(exercise)
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//    }

}
