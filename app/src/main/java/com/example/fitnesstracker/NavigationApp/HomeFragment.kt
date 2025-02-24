package com.example.fitnesstracker.NavigationApp

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.DialogAddWorkoutBinding
import com.example.fitnesstracker.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: WorkoutAdapter
    private val daysList = mutableListOf<String>()
    private val exercisesMap = mutableMapOf<String, MutableList<String>>()

    private val allExercises = listOf(
        "Push-Up", "Squat", "Bench Press", "Deadlift", "Pull-Up", "Lunges", "Plank", "Burpees",
        "Mountain Climbers", "Jumping Jacks", "Russian Twists", "Bicycle Crunches", "Dips", "Leg Raises",
        "Side Plank", "Superman", "Kettlebell Swings", "Hip Thrusts", "Box Jumps", "Wall Sit", "Farmer’s Walk",
        "Jump Rope", "Medicine Ball Slams", "Sled Push", "Cable Rows", "Tricep Extensions", "Bicep Curls",
        "Face Pulls", "Hamstring Curls", "Glute Bridges", "Single-Leg Deadlift", "Arnold Press", "Chest Fly",
        "Hack Squat", "Overhead Squat", "Hanging Leg Raises", "Turkish Get-Up", "Calf Raises", "Tire Flips",
        "Battle Ropes", "Reverse Lunges", "Good Mornings", "Step-Ups", "Incline Dumbbell Press", "Seated Shoulder Press",
        "Snatch", "Clean and Jerk", "Zercher Squat"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        loadSavedData()

        adapter = WorkoutAdapter(daysList, exercisesMap, ::onDeleteDay, ::onDeleteExercise)
        binding.recyclerViewWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkouts.adapter = adapter
        binding.suggestionWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutPlansFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnAddDay.setOnClickListener { addWorkoutDay() }
        binding.fabAddWorkout.setOnClickListener { showWorkoutDialog() }
        binding.suggestionWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutPlansFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun addWorkoutDay() {
        val dayName = binding.etWorkoutDay.text.toString().trim()
        if (dayName.isNotEmpty() && !daysList.contains(dayName)) {
            daysList.add(dayName)
            exercisesMap[dayName] = mutableListOf()
            adapter.notifyDataSetChanged()
            saveData()
            binding.etWorkoutDay.text.clear()
        } else {
            Toast.makeText(requireContext(), "Enter a valid day name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWorkoutDialog() {
        val dialogBinding = DialogAddWorkoutBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Add Workout")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ -> addWorkout(dialogBinding) }
            .setNegativeButton("Cancel", null)

        dialogBinding.spinnerDays.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, daysList)

        dialogBinding.etSearchExercise.apply {
            setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allExercises)
            )
            threshold = 1
        }

        builder.show()
    }

    private fun addWorkout(dialogBinding: DialogAddWorkoutBinding) {
        val selectedDay = dialogBinding.spinnerDays.selectedItem?.toString()
        val selectedExercise = dialogBinding.etSearchExercise.text.toString()

        if (!selectedDay.isNullOrEmpty() && selectedExercise in allExercises) {
            exercisesMap[selectedDay]?.add(selectedExercise)
            adapter.notifyDataSetChanged()
            saveData()
        } else {
            Toast.makeText(requireContext(), "Please select a valid day and exercise", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDeleteDay(day: String) {
        daysList.remove(day)
        exercisesMap.remove(day)
        adapter.notifyDataSetChanged()
        saveData()
    }

    private fun onDeleteExercise(day: String, exercise: String) {
        exercisesMap[day]?.remove(exercise)
        adapter.notifyDataSetChanged()
        saveData()
    }

    private fun saveData() {
        sharedPreferences.edit()
            .putString("daysList", Gson().toJson(daysList))
            .putString("exercisesMap", Gson().toJson(exercisesMap))
            .apply()
    }

    private fun loadSavedData() {
        daysList.clear()
        exercisesMap.clear()

        val daysJson = sharedPreferences.getString("daysList", "[]")
        val exercisesJson = sharedPreferences.getString("exercisesMap", "{}")

        val typeDays = object : TypeToken<MutableList<String>>() {}.type
        val typeExercises = object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type

        daysList.addAll(Gson().fromJson(daysJson, typeDays))
        exercisesMap.putAll(Gson().fromJson(exercisesJson, typeExercises))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

