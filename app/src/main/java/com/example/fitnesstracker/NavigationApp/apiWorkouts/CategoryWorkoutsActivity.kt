package com.example.fitnesstracker.NavigationApp.apiWorkouts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.fitnesstracker.NavigationApp.apiWorkouts.AppDatabase.Companion.migration_1_2
import com.example.fitnesstracker.databinding.ActivityCategoryWorkoutsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        exerciseAdapter = ExerciseApiAdapter(
            exercises = ArrayList(),
            onFavoriteClick = { exercise ->
                toggleFavorite(exercise)
            },
            onItemClick = { exercise ->
                openExerciseDetails(exercise)
            },
        )

        binding.recyclerViewExercises.adapter = exerciseAdapter



        val bodyPart = intent.getStringExtra("BODY_PART")
        if (bodyPart.isNullOrEmpty()) {
            Toast.makeText(this, "No category received!", Toast.LENGTH_SHORT).show()
            return
        }

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "exercise_db")
            .addMigrations(migration_1_2)  // إضافة الترحيل
            .build()

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
                    instructions = it.instructions.split(" | "),
                    isFavorite = it.isFavorite,
                    id = it.id.toString()
                )
            }
            withContext(Dispatchers.Main) {
                exerciseAdapter.updateExercises(mapped)
            }
        }
    }
    private fun openExerciseDetails(exercise: Exercise) {
        val intent = Intent(this, ExerciseDetailsActivity::class.java).apply {
            putExtra("name", exercise.name)
            putExtra("target", exercise.target)
            putExtra("gifUrl", exercise.gifUrl)
            putExtra("secondaryMuscles", exercise.secondaryMuscles.joinToString(", "))
            putExtra("bodyPart", exercise.bodyPart)
            putStringArrayListExtra("instructions", ArrayList(exercise.instructions))
        }
        startActivity(intent)

    }
    private fun toggleFavorite(exercise: Exercise) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoritesRef = Firebase.firestore
            .collection("users")
            .document(userId)
            .collection("favorites")

        if (exercise.isFavorite) {
            favoritesRef.document(exercise.name).delete()
        } else {
            favoritesRef.document(exercise.name).set(exercise)
        }
    }
}

