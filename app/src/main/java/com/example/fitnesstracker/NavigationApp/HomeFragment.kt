package com.example.fitnesstracker.NavigationApp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//
    private val workoutDays = mutableMapOf<String, MutableList<String>>()
    private val gymExercises = listOf(
        "None", "Bench Press", "Incline Bench Press", "Dumbbell Fly", "Cable Crossover", "Push-ups", "Dips",
        "Shoulder Press", "Arnold Press", "Lateral Raises", "Front Raises", "Face Pulls", "Shrugs",
        "Pull-ups", "Lat Pulldown", "Bent-over Rows", "Deadlift", "Seated Cable Row", "T-Bar Row",
        "Bicep Curls", "Hammer Curls", "Triceps Dips", "Triceps Pushdown", "Skull Crushers", "Concentration Curl",
        "Squat", "Leg Press", "Lunges", "Bulgarian Split Squat", "Romanian Deadlift", "Calf Raises",
        "Hip Thrust", "Glute Bridges", "Cable Kickbacks", "Sumo Deadlift", "Plank", "Russian Twists",
        "Bicycle Crunches", "Ab Rollouts", "Sit-ups", "Chest Fly Machine", "Leg Extension", "Leg Curl",
        "Reverse Crunches", "Hanging Leg Raises", "Battle Ropes", "Kettlebell Swings"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWorkoutDays()
        updateUI()

        binding.btnAddDay.setOnClickListener {
            val dayName = binding.etDayName.text.toString().trim()
            if (dayName.isNotEmpty()) {
                addWorkoutDay(dayName)
                binding.etDayName.text.clear()
            }
        }

        binding.fabAddExercise.setOnClickListener {
            showSelectDayDialog()
        }
    }

    private fun addWorkoutDay(dayName: String) {
        if (!workoutDays.containsKey(dayName)) {
            workoutDays[dayName] = mutableListOf()
            saveWorkoutDays()
        }
        updateUI()
    }

    private fun showSelectDayDialog() {
        val dayNames = workoutDays.keys.toTypedArray()
        if (dayNames.isEmpty()) {
            Toast.makeText(requireContext(), "No workout days added!", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Select a Day")
            .setItems(dayNames) { _, which -> showAddExerciseDialog(dayNames[which]) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddExerciseDialog(dayName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_exercise, null)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchView)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerExercises)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, gymExercises)
        spinner.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = gymExercises.filter { it.contains(newText.orEmpty(), ignoreCase = true) }
                (spinner.adapter as ArrayAdapter<String>).apply {
                    clear()
                    addAll(filteredList)
                    notifyDataSetChanged()
                }
                return true
            }
        })

        AlertDialog.Builder(requireContext())
            .setTitle("Add Exercise to $dayName")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val exercise = spinner.selectedItem.toString()
                if (exercise != "None") {
                    workoutDays[dayName]?.add(exercise)
                    saveWorkoutDays()
                    updateUI()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateUI() {
        binding.layoutWorkoutDays.removeAllViews()
        for ((dayName, exercises) in workoutDays) {
            val dayLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(10, 10, 10, 10)
            }

            val dayButton = Button(requireContext()).apply {
                text = dayName
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val exerciseList = TextView(requireContext()).apply {
                text = exercises.joinToString("\n")
                setPadding(5, 5, 5, 5)
            }

            dayLayout.addView(dayButton)
            dayLayout.addView(exerciseList)
            binding.layoutWorkoutDays.addView(dayLayout)
        }
    }

    private fun saveWorkoutDays() {
        val sharedPreferences = requireContext().getSharedPreferences("WorkoutData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("workoutDays", workoutDays.mapValues { it.value.toSet() }.map { "${it.key}:${it.value.joinToString(",")}" }.toSet())
        editor.apply()
    }

    private fun loadWorkoutDays() {
        val sharedPreferences = requireContext().getSharedPreferences("WorkoutData", Context.MODE_PRIVATE)
        val savedData = sharedPreferences.getStringSet("workoutDays", emptySet())
        workoutDays.clear()
        savedData?.forEach {
            val parts = it.split(":")
            if (parts.size == 2) {
                val day = parts[0]
                val exercises = parts[1].split(",").toMutableList()
                workoutDays[day] = exercises
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

