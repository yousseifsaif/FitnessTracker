package com.example.fitnesstracker.NavigationApp.ProfileFields

import ButtonClickUtil
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivityEditProfileBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.calculateCal
import com.example.fitnesstracker.toast.showToast
import com.example.fitnesstracker.toast.updateOrientationLock
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
            binding.tvUserName.text = updatedUser.name
            binding.tvAge.text = updatedUser.age.toString()
            binding.tvUserEmail.text = updatedUser.email
            binding.tvKcal.text = updatedUser.calories.toString()
            binding.tvHeight.text = "${updatedUser.height} "
            binding.tvWeight.text = "${updatedUser.weight} "
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

        var lastClickTime: Long = 0

        binding.updateEditProfile.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastClickTime < 1000) {
                    showToast(this, "Please wait before clicking again")
                    return@preventSpamClick
                }

                lastClickTime = currentTime

                binding.updateEditProfile.isEnabled = false

                val name = nameEt.text.toString().trim()
                val email = emailEt.text.toString().trim()

                if (name.isEmpty() || email.isEmpty()) {
                    showToast(this, "Name and Email cannot be empty")
                    binding.updateEditProfile.isEnabled = true
                    return@preventSpamClick
                }

                val age = ageEt.text.toString().toIntOrNull()
                if (age == null || age !in 1..120) {
                    showToast(this, "Please enter a valid age between 1 and 100")
                    binding.updateEditProfile.isEnabled = true
                    return@preventSpamClick
                }

                val height = heightEt.text.toString().toIntOrNull()
                if (height == null || height !in 1..200) {
                    showToast(this, "Please enter a valid height between 1 cm and 200 cm")
                    binding.updateEditProfile.isEnabled = true
                    return@preventSpamClick
                }

                val weight = weightEt.text.toString().toIntOrNull()
                if (weight == null || weight !in 1..200) {
                    showToast(this, "Please enter a valid weight between 1 kg and 200 kg")
                    binding.updateEditProfile.isEnabled = true
                    return@preventSpamClick
                }

                val updatedUser = SharedPrefHelper.User(
                    name = name,
                    email = email,
                    age = age,
                    height = height,
                    weight = weight,
                    gender = userData.gender,
                    password = userData.password,
                    id = userData.id,
                    selectedGoal = userData.selectedGoal,
                    weighttype = userData.weighttype,
                    ActivityLevel = userData.ActivityLevel,
                    isLoggedIn = userData.isLoggedIn,
                    calories = 0
                )

                val updatedUserWithCalories = updatedUser.copy(
                    calories = calculateCal(updatedUser).toInt()
                )

                sharedPref.saveUserToPrefs(updatedUserWithCalories)

                db.collection("users").document(userData.id).set(updatedUserWithCalories)
                    .addOnSuccessListener {
                        showToast(this, "Profile updated successfully!")
                        binding.updateEditProfile.isEnabled = true
                        finish()
                    }.addOnFailureListener {
                        showToast(this, "Failed to update profile")
                        binding.updateEditProfile.isEnabled = true
                    }
                userViewModel.updateUser(updatedUserWithCalories)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}