package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.apiWorkouts.*
import com.example.fitnesstracker.databinding.ActivityCategoryWorkoutsBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CategoryWorkoutsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryWorkoutsBinding
    private lateinit var exerciseAdapter: ExerciseApiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bodyPart = intent.getStringExtra("BODY_PART")
        Log.d("CATEGORY_INTENT", "Received bodyPart: $bodyPart")
        if (bodyPart.isNullOrEmpty()) {
            Log.e("CATEGORY_INTENT", "No bodyPart received! Using default value.")
            Toast.makeText(this, "No category received!", Toast.LENGTH_SHORT).show()
            return
        }

        exerciseAdapter = ExerciseApiAdapter(ArrayList())
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExercises.adapter = exerciseAdapter

        getExercises(bodyPart)
    }

    private fun getExercises(bodyPart: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiCallable::class.java)
        Log.d("API_REQUEST", "Fetching exercises for bodyPart: $bodyPart")

        api.getExercises(bodyPart).enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                Log.d("API_RESPONSE", "Response Code: ${response.code()}")
                Log.d("API_RESPONSE", "Response Body: ${response.body()}")

                if (response.isSuccessful) {
                    val exercises = response.body()
                    if (!exercises.isNullOrEmpty()) {
                        exerciseAdapter.updateExercises(ArrayList(exercises))
                    } else {
                        Log.e("API_RESPONSE", "No exercises found!")
                        Toast.makeText(applicationContext, "No exercises found!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Log.e("API_RESPONSE", "Failure: ${t.message}")
                Toast.makeText(applicationContext, "API request failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}