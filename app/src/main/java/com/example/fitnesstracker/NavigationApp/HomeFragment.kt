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
        "Push-Up", "Wide Push-Up", "Diamond Push-Up", "Archer Push-Up", "Pike Push-Up",
        "One-Arm Push-Up", "Bench Press", "Incline Bench Press", "Decline Bench Press",
        "Dumbbell Bench Press", "Chest Fly", "Cable Crossovers", "Dips", "Triceps Dips",
        "Skull Crushers", "Triceps Kickbacks", "Close-Grip Bench Press", "Overhead Triceps Extension",
        "Face Pulls", "Bicep Curls", "Hammer Curls", "Concentration Curls", "Preacher Curls",
        "Zottman Curls", "Chin-Ups", "Pull-Ups", "Lat Pulldown", "Cable Rows",
        "One-Arm Dumbbell Row", "Bent-Over Barbell Row", "Reverse Fly", "T-Bar Row",
        "Shrugs", "Upright Rows", "Overhead Shoulder Press", "Seated Dumbbell Shoulder Press",
        "Military Press", "Arnold Press", "Lateral Raises", "Front Raises", "Reverse Pec Deck",
        "Clean and Jerk", "Snatch", "Hanging Leg Raises", "Squat", "Front Squat",
        "Hack Squat", "Overhead Squat", "Goblet Squat", "Sumo Squat", "Zercher Squat",
        "Split Squat", "Bulgarian Split Squat", "Pistol Squat", "Step-Ups", "Box Jumps",
        "Jump Squats", "Wall Sit", "Leg Press", "Hip Thrusts", "Glute Bridges",
        "Romanian Deadlift", "Single-Leg Deadlift", "Conventional Deadlift", "Sumo Deadlift",
        "Trap Bar Deadlift", "Good Mornings", "Hamstring Curls", "Lying Hamstring Curls",
        "Standing Calf Raises", "Seated Calf Raises", "Donkey Calf Raises", "Reverse Lunges",
        "Forward Lunges", "Side Lunges", "Curtsy Lunges", "Jumping Lunges", "Sled Push",
        "Farmer’s Walk", "Hip Abductions", "Hip Adductions", "Burpees", "Mountain Climbers",
        "Jumping Jacks", "Battle Ropes", "Medicine Ball Slams", "Tire Flips", "Turkish Get-Up",
        "Kettlebell Swings", "Thrusters", "Man Makers", "Sledgehammer Swings", "Bear Crawls",
        "Dead Hang", "Hanging Knee Raises", "Hanging Windshield Wipers", "Plank", "Side Plank",
        "Superman", "Russian Twists", "Bicycle Crunches", "Hanging Leg Raises",
        "Hanging Knee Tucks", "Cable Crunches", "Ab Rollouts", "Reverse Crunches", "V-Ups",
        "Hollow Body Hold", "Jackknife Sit-Ups", "Flutter Kicks", "Toe Touches", "Woodchoppers",
        "Landmine Twists", "Hanging Windshield Wipers", "Oblique Twists", "Running", "Sprinting",
        "Treadmill Intervals", "Jump Rope", "Rowing Machine", "Cycling", "Swimming", "High Knees",
        "Skater Jumps", "Stair Climbing", "Shadow Boxing", "Heavy Bag Work", "Speed Ladder Drills",
        "Agility Cone Drills", "Jumping Rope Double Unders", "Jump Rope Crisscross",
        "Jump Rope Side Swings", "Battle Ropes Waves", "Battle Ropes Slams", "Bear Crawls",
        "Sledgehammer Swings", "Lat Pulldown Machine", "Chest Press Machine", "Seated Row Machine",
        "Leg Curl Machine", "Leg Extension Machine", "Hack Squat Machine", "Glute Kickback Machine",
        "Cable Lateral Raises", "Cable Rear Delt Fly", "Assisted Pull-Ups", "Assisted Dips",
        "Ab Crunch Machine", "Static Stretching", "Dynamic Stretching", "Foam Rolling",
        "Yoga Poses", "Cat-Cow Stretch", "Cobra Stretch", "Downward Dog", "Seated Forward Fold",
        "Standing Toe Touch", "Pigeon Pose", "Butterfly Stretch", "Shoulder Mobility Drills",
        "Hip Openers", "Thoracic Spine Rotations", "Ankle Mobility Drills",
        "Resistance Band Pull-Aparts", "Resistance Band Squats", "Resistance Band Deadlifts",
        "Resistance Band Rows", "Resistance Band Face Pulls", "Resistance Band Bicep Curls",
        "Resistance Band Triceps Extensions", "Resistance Band Lateral Walks",
        "Resistance Band Glute Kickbacks", "Resistance Band Chest Press", "Resistance Band Pallof Press",
        "Resistance Band Overhead Press", "Handstand Push-Ups", "Planche Holds", "Front Lever",
        "Back Lever", "Human Flag", "L-Sit Holds", "Ring Dips", "Ring Muscle-Ups", "Bar Muscle-Ups",
        "Skin the Cat", "Clapping Push-Ups", "Explosive Pull-Ups", "One-Arm Pull-Ups",
        "Atlas Stone Lifts", "Log Press", "Yoke Carry", "Conan’s Wheel", "Axle Deadlifts",
        "Circus Dumbbell Press", "Car Deadlift", "Stone to Shoulder", "Viking Press",
        "Silver Dollar Deadlift", "Fingal’s Fingers"
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

        sharedPreferences =
            requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
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
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allExercises
                )
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
            Toast.makeText(
                requireContext(),
                "Please select a valid day and exercise",
                Toast.LENGTH_SHORT
            ).show()
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

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

