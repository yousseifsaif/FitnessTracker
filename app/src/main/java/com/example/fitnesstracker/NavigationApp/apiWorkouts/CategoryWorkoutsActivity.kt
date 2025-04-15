package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.fitnesstracker.databinding.ActivityCategoryWorkoutsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CategoryWorkoutsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryWorkoutsBinding
    private lateinit var exerciseAdapter: ExerciseApiAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bodyPart = intent.getStringExtra("BODY_PART")
        if (bodyPart.isNullOrEmpty()) {
            Toast.makeText(this, "No category received!", Toast.LENGTH_SHORT).show()
            return
        }

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "exercise_db")
            .fallbackToDestructiveMigration()
            .build()

        exerciseAdapter = ExerciseApiAdapter(ArrayList())
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExercises.adapter = exerciseAdapter

        fetchExercisesFromApiOrRoom(bodyPart)
    }

    private fun fetchExercisesFromApiOrRoom(bodyPart: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiCallable::class.java)

        api.getExercises(bodyPart).enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                if (response.isSuccessful) {
                    val exercises = response.body()
                    if (!exercises.isNullOrEmpty()) {
                        saveExercisesToRoom(exercises)
                    } else {
                        Toast.makeText(applicationContext, "No exercises found!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    loadFromRoom() // fallback if error
                    Toast.makeText(applicationContext, "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Log.e("API_FAILURE", t.message ?: "Unknown error")
                Toast.makeText(applicationContext, "if all data doesn't show try again and check internet connection", Toast.LENGTH_SHORT).show()
                loadFromRoom()
            }
        })
    }

    private fun saveExercisesToRoom(exercises: List<Exercise>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = db.exerciseDao()
            dao.clearExercises() // clear old data
            exercises.forEach {
                val entity = ExerciseEntity(
                    name = it.name,
                    bodyPart = it.bodyPart,
                    equipment = it.equipment,
                    target = it.target,
                    gifUrl = it.gifUrl,
                    secondaryMuscles = it.secondaryMuscles.joinToString(", "),
                    instructions = it.instructions.joinToString(" | ")
                )
                dao.insertExercise(entity)
            }
            loadFromRoom()
        }
    }

    private fun loadFromRoom() {
        lifecycleScope.launch(Dispatchers.IO) {
            val localData = db.exerciseDao().getAllExercises()
            val mapped = localData.map {
                Exercise(
                    name = it.name,
                    bodyPart = it.bodyPart,
                    equipment = it.equipment,
                    target = it.target,
                    gifUrl = it.gifUrl,
                    secondaryMuscles = it.secondaryMuscles.split(", "),
                    instructions = it.instructions.split(" | ")
                )
            }
            withContext(Dispatchers.Main) {
                exerciseAdapter.updateExercises(mapped)
            }
        }
    }
}
