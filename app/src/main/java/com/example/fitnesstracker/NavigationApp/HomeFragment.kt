package com.example.fitnesstracker.NavigationApp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.ProfileFields.FavoritesActivity
import com.example.fitnesstracker.NavigationApp.chatAi.ChatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.databinding.DialogAddWorkoutBinding
import com.example.fitnesstracker.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.jvm.java

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: WorkoutAdapter
    private val daysList = mutableListOf<String>()
    private val exercisesMap = mutableMapOf<String, MutableList<String>>()
    private lateinit var db: FirebaseFirestore
    private val allExercises = listOf(
        "Push-Up",
        "Wide Push-Up",
        "Diamond Push-Up",
        "Archer Push-Up",
        "Pike Push-Up",
        "One-Arm Push-Up",
        "Bench Press",
        "Incline Bench Press",
        "Decline Bench Press",
        "Dumbbell Bench Press",
        "Chest Fly",
        "Cable Crossovers",
        "Dips",
        "Triceps Dips",
        "Skull Crushers",
        "Triceps Kickbacks",
        "Close-Grip Bench Press",
        "Overhead Triceps Extension",
        "Face Pulls",
        "Bicep Curls",
        "Hammer Curls",
        "Concentration Curls",
        "Preacher Curls",
        "Zottman Curls",
        "Chin-Ups",
        "Pull-Ups",
        "Lat Pulldown",
        "Cable Rows",
        "One-Arm Dumbbell Row",
        "Bent-Over Barbell Row",
        "Reverse Fly",
        "T-Bar Row",
        "Shrugs",
        "Upright Rows",
        "Overhead Shoulder Press",
        "Seated Dumbbell Shoulder Press",
        "Military Press",
        "Arnold Press",
        "Lateral Raises",
        "Front Raises",
        "Reverse Pec Deck",
        "Clean and Jerk",
        "Snatch",
        "Hanging Leg Raises",
        "Squat",
        "Front Squat",
        "Hack Squat",
        "Overhead Squat",
        "Goblet Squat",
        "Sumo Squat",
        "Zercher Squat",
        "Split Squat",
        "Bulgarian Split Squat",
        "Pistol Squat",
        "Step-Ups",
        "Box Jumps",
        "Jump Squats",
        "Wall Sit",
        "Leg Press",
        "Hip Thrusts",
        "Glute Bridges",
        "Romanian Deadlift",
        "Single-Leg Deadlift",
        "Conventional Deadlift",
        "Sumo Deadlift",
        "Trap Bar Deadlift",
        "Good Mornings",
        "Hamstring Curls",
        "Lying Hamstring Curls",
        "Standing Calf Raises",
        "Seated Calf Raises",
        "Donkey Calf Raises",
        "Reverse Lunges",
        "Forward Lunges",
        "Side Lunges",
        "Curtsy Lunges",
        "Jumping Lunges",
        "Sled Push",
        "Farmer’s Walk",
        "Hip Abductions",
        "Hip Adductions",
        "Burpees",
        "Mountain Climbers",
        "Jumping Jacks",
        "Battle Ropes",
        "Medicine Ball Slams",
        "Tire Flips",
        "Turkish Get-Up",
        "Kettlebell Swings",
        "Thrusters",
        "Man Makers",
        "Sledgehammer Swings",
        "Bear Crawls",
        "Dead Hang",
        "Hanging Knee Raises",
        "Hanging Windshield Wipers",
        "Plank",
        "Side Plank",
        "Superman",
        "Russian Twists",
        "Bicycle Crunches",
        "Hanging Leg Raises",
        "Hanging Knee Tucks",
        "Cable Crunches",
        "Ab Rollouts",
        "Reverse Crunches",
        "V-Ups",
        "Hollow Body Hold",
        "Jackknife Sit-Ups",
        "Flutter Kicks",
        "Toe Touches",
        "Woodchoppers",
        "Landmine Twists",
        "Hanging Windshield Wipers",
        "Oblique Twists",
        "Running",
        "Sprinting",
        "Treadmill Intervals",
        "Jump Rope",
        "Rowing Machine",
        "Cycling",
        "Swimming",
        "High Knees",
        "Skater Jumps",
        "Stair Climbing",
        "Shadow Boxing",
        "Heavy Bag Work",
        "Speed Ladder Drills",
        "Agility Cone Drills",
        "Jumping Rope Double Unders",
        "Jump Rope Crisscross",
        "Jump Rope Side Swings",
        "Battle Ropes Waves",
        "Battle Ropes Slams",
        "Bear Crawls",
        "Sledgehammer Swings",
        "Lat Pulldown Machine",
        "Chest Press Machine",
        "Seated Row Machine",
        "Leg Curl Machine",
        "Leg Extension Machine",
        "Hack Squat Machine",
        "Glute Kickback Machine",
        "Cable Lateral Raises",
        "Cable Rear Delt Fly",
        "Assisted Pull-Ups",
        "Assisted Dips",
        "Ab Crunch Machine",
        "Static Stretching",
        "Dynamic Stretching",
        "Foam Rolling",
        "Yoga Poses",
        "Cat-Cow Stretch",
        "Cobra Stretch",
        "Downward Dog",
        "Seated Forward Fold",
        "Standing Toe Touch",
        "Pigeon Pose",
        "Butterfly Stretch",
        "Shoulder Mobility Drills",
        "Hip Openers",
        "Thoracic Spine Rotations",
        "Ankle Mobility Drills",
        "Resistance Band Pull-Aparts",
        "Resistance Band Squats",
        "Resistance Band Deadlifts",
        "Resistance Band Rows",
        "Resistance Band Face Pulls",
        "Resistance Band Bicep Curls",
        "Resistance Band Triceps Extensions",
        "Resistance Band Lateral Walks",
        "Resistance Band Glute Kickbacks",
        "Resistance Band Chest Press",
        "Resistance Band Pallof Press",
        "Resistance Band Overhead Press",
        "Handstand Push-Ups",
        "Planche Holds",
        "Front Lever",
        "Back Lever",
        "Human Flag",
        "L-Sit Holds",
        "Ring Dips",
        "Ring Muscle-Ups",
        "Bar Muscle-Ups",
        "Skin the Cat",
        "Clapping Push-Ups",
        "Explosive Pull-Ups",
        "One-Arm Pull-Ups",
        "Atlas Stone Lifts",
        "Log Press",
        "Yoke Carry",
        "Conan’s Wheel",
        "Axle Deadlifts",
        "Circus Dumbbell Press",
        "Car Deadlift",
        "Stone to Shoulder",
        "Viking Press",
        "Silver Dollar Deadlift",
        "Fingal’s Fingers"
    )
    private var isFabOpen = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        sharedPreferences =
            requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        loadSavedData()

        adapter = WorkoutAdapter(daysList, exercisesMap, ::onDeleteDay, ::onDeleteExercise)
        binding.recyclerViewWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkouts.adapter = adapter
        setupFab()
        binding.btnAddDay.setOnClickListener { showAddDayDialog() }
        binding.addWorkout.setOnClickListener {
            showWorkoutDialog()
        }
        binding.fabFav.setOnClickListener {
            val intent = Intent(requireContext(), FavoritesActivity::class.java)
            startActivity(intent)
        }

        binding.fabMain.setOnClickListener {
            if (!isFabOpen) {
                showFab()
            } else {
                hideFab()
            }
        }


        binding.addWorkout.setOnClickListener {
            showWorkoutDialog()
        }
        binding.fabSearch.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)
        }
        binding.suggestionWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutPlansFragment())
                .addToBackStack(null)
                .commit()
        }


        fetchDataFromFirestore()
    }

//    private fun addWorkoutDay() {
//        val dayName = binding.etWorkoutDay.text.toString().trim()
//        if (dayName.isNotEmpty() && !daysList.contains(dayName)) {
//            daysList.add(dayName)
//            exercisesMap[dayName] = mutableListOf()
//            adapter.notifyDataSetChanged()
//            saveData()
//            binding.etWorkoutDay.text.clear()
//            saveDataToFirestore()
//        } else {
//            Toast.makeText(requireContext(), "Enter a valid day name", Toast.LENGTH_SHORT).show()
//        }
//    }

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
            saveDataToFirestore()
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
        saveDataToFirestore()
    }

    private fun onDeleteExercise(day: String, exercise: String) {
        exercisesMap[day]?.remove(exercise)
        adapter.notifyDataSetChanged()
        saveData()
        saveDataToFirestore()
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

    private fun saveDataToFirestore() {
        val exercisesMapForFirestore = exercisesMap.mapValues { it.value.toList() }

        val workoutData = mapOf(
            "daysList" to daysList,
            "exercisesMap" to exercisesMapForFirestore
        )

        db.collection("workouts")
            .document("user_workout_data")
            .set(workoutData)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save", Toast.LENGTH_SHORT).show()
            }
    }


    private fun fetchDataFromFirestore() {
        db.collection("workouts")
            .document("user_workout_data")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val data = document.data
                    if (data != null) {
                        val daysListFirestore = data["daysList"] as? List<*>
                        val exercisesMapFirestore = data["exercisesMap"] as? Map<*, *>

                        if (daysListFirestore != null) {
                            daysList.clear()
                            exercisesMap.clear()

                            daysList.addAll(daysListFirestore.filterIsInstance<String>())

                            exercisesMapFirestore?.forEach { (day, exercises) ->
                                if (day is String && exercises is List<*>) {
                                    exercisesMap[day] =
                                        exercises.filterIsInstance<String>().toMutableList()
                                }
                            }

                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFab() {
        val fab = binding.fabMain
        val nestedScroll = binding.nestedScrollView

        nestedScroll.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) fab.hide()
            else if (scrollY < oldScrollY) fab.show()
        }

        fab.setOnClickListener {
            if (!isFabOpen) showFab()
            else hideFab()
        }
    }

    private fun showFab() {
        isFabOpen = true
        binding.fabMain.setImageResource(R.drawable.cancel)


        binding.fabSearch.visibility = View.VISIBLE
        binding.addWorkout.visibility = View.VISIBLE
        binding.fabFav.visibility = View.VISIBLE

        binding.fabSearch.alpha = 0f
        binding.addWorkout.alpha = 0f
        binding.fabFav.alpha = 0f

        binding.fabSearch.animate().alpha(1f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        binding.addWorkout.animate().alpha(1f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        binding.fabFav.animate().alpha(1f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun hideFab() {
        isFabOpen = false
        binding.fabMain.setImageResource(R.drawable.add)

        binding.fabSearch.animate().alpha(0f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { binding.fabSearch.visibility = View.INVISIBLE }
            .start()

        binding.addWorkout.animate().alpha(0f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { binding.addWorkout.visibility = View.INVISIBLE }
            .start()
        binding.fabFav.animate().alpha(0f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { binding.fabFav.visibility = View.INVISIBLE }
            .start()
    }

    private fun showAddDayDialog() {
        val input = EditText(requireContext()).apply {
            hint = "e.g. Chest Day"
            setPadding(50, 30, 50, 30)
            background = ContextCompat.getDrawable(context, R.drawable.edittext_background)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Workout Day")
            .setMessage("Enter a unique name for your workout day.")

            .setView(input)
//            .setIcon(R.drawable.ic_calendar_add)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)

            messageTextView?.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                setPadding(80, 16, 32, 16)

            }
            titleTextView?.apply {
                gravity = Gravity.CENTER
            }
            button.setOnClickListener {
                val dayName = input.text.toString().trim()
                if (dayName.isNotEmpty() && !daysList.contains(dayName)) {
                    daysList.add(dayName)
                    exercisesMap[dayName] = mutableListOf()
                    adapter.notifyDataSetChanged()
                    saveData()
                    saveDataToFirestore()
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter a unique, valid day name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.rgb(35, 35, 35)))

    }

    private fun saveAllData() {
        saveData()
        saveDataToFirestore()
    }
}
