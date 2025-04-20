package com.example.fitnesstracker.NavigationApp.ProfileFields

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.apiWorkouts.Exercise
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseApiAdapter
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseDetailsActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityFavoritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: ExerciseApiAdapter
    private val db = Firebase.firestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE)

        adapter = ExerciseApiAdapter(
            exercises = emptyList(),
            onFavoriteClick = { exercise ->
                removeFavorite(exercise)
            },
            onItemClick = { exercise ->
                val intent = Intent(this, ExerciseDetailsActivity::class.java)
                startActivity(intent)
            },
            sharedPreferences = sharedPreferences
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val favorites = result.toObjects(Exercise::class.java).map { exercise ->
                    exercise.copy(isFavorite = true)
                }
                adapter.updateExercises(favorites)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFavorite(exercise: Exercise) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users").document(userId).collection("favorites")
            .document(exercise.name)
            .delete()
            .addOnSuccessListener {
                sharedPreferences.edit()
                    .putBoolean("fav_${exercise.id}", false)
                    .apply()

                loadFavorites()
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show()
            }
    }
}
