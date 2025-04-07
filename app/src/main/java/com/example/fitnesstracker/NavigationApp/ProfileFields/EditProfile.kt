package com.example.fitnesstracker.NavigationApp.ProfileFields

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.databinding.ActivityEditProfileBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EditProfile : AppCompatActivity() {
    private val db = Firebase.firestore
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = SharedPrefHelper(this)
        val userData = sharedPref.getUserFromPrefs()

        userViewModel.updateUser(userData)

        userViewModel.user.observe(this) { updatedUser ->
            binding.tvUserName.setText(updatedUser.name)
            binding.tvAge.setText(updatedUser.age.toString())
            binding.tvUserEmail.setText(updatedUser.email)
            binding.tvHeight.setText("${updatedUser.height} ")
            binding.tvWeight.setText("${updatedUser.weight} ")
            binding.nameEditText.setText(updatedUser.name)
            binding.emailEditText.setText(updatedUser.email)
            binding.AgeEditText.setText(updatedUser.age.toString())
            binding.HeightEditText.setText(updatedUser.height.toString())
            binding.WeightEditText.setText(updatedUser.weight.toString())
        }
        val nameEt = binding.nameEditText
        val emailEt = binding.emailEditText
        val heightEt = binding.HeightEditText
        val weightEt = binding.WeightEditText
        val ageEt = binding.AgeEditText

        binding.updateEditProfile.setOnClickListener {
            if (nameEt.text.isNullOrEmpty() || emailEt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedUser = SharedPrefHelper.User(
                name = nameEt.text.toString(),
                email = emailEt.text.toString(),
                age = ageEt.text.toString().toIntOrNull() ?: userData.age,
                height = heightEt.text.toString().toIntOrNull() ?: userData.height,
                weight = weightEt.text.toString().toIntOrNull() ?: userData.weight,
                gender = userData.gender,
                password = userData.password,
                id = userData.id,
                selectedGoal = userData.selectedGoal,
                weighttype = userData.weighttype,
                calories = userData.calories,
                ActivityLevel = userData.ActivityLevel,
                isLoggedIn = userData.isLoggedIn
            )

            sharedPref.saveUserToPrefs(updatedUser)

            db.collection("users").document(userData.id).set(updatedUser).addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }

            userViewModel.updateUser(updatedUser)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()


        }
    }
}