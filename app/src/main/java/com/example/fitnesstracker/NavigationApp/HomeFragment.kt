package com.example.fitnesstracker.NavigationApp

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val workoutDays = mutableMapOf<String, LinearLayout>()
    private val favoriteExercises = listOf("Push-up", "Squat", "Deadlift", "Bench Press", "Pull-up")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        loadWorkoutDays()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddDay.setOnClickListener {
            val dayName = binding.etDayName.text.toString().trim()
            if (dayName.isNotEmpty()) {
                addWorkoutDay(dayName)
                binding.etDayName.text.clear()
                saveWorkoutDays()
            }
        }

        binding.fabAddExercise.setOnClickListener {
            showSelectDayDialog()
        }
    }

    private fun addWorkoutDay(dayName: String) {
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
            setOnLongClickListener {
                animateDelete(dayLayout) { deleteWorkoutDay(dayName) }
                true
            }
        }

        val exerciseList = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        dayLayout.addView(dayButton)
        dayLayout.addView(exerciseList)
        binding.layoutWorkoutDays.addView(dayLayout)

        workoutDays[dayName] = exerciseList
    }

    private fun showSelectDayDialog() {
        if (workoutDays.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("No Workout Days")
                .setMessage("Please add a workout Name first.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
            return
        }

        val days = workoutDays.keys.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Select Workout Day")
            .setItems(days) { _, which ->
                showExerciseSelectionDialog(days[which])
            }
            .show()
    }

    private fun showExerciseSelectionDialog(dayName: String) {
        val selectedExercises = mutableListOf<String>()
        val exercisesArray = favoriteExercises.toTypedArray()
        val checkedItems = BooleanArray(exercisesArray.size)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Exercises")
            .setMultiChoiceItems(exercisesArray, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedExercises.add(exercisesArray[which])
                } else {
                    selectedExercises.remove(exercisesArray[which])
                }
            }
            .setPositiveButton("Add") { _, _ ->
                addExercisesToDay(dayName, selectedExercises)
                saveWorkoutDays()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun addExercisesToDay(dayName: String, exercises: List<String>) {
        val exerciseList = workoutDays[dayName] ?: return
        exercises.forEach { exercise ->
            val exerciseText = TextView(requireContext()).apply {
                text = "• $exercise"
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(20, 5, 0, 5)
                setOnLongClickListener {
                    animateDelete(this) { exerciseList.removeView(this); saveWorkoutDays() }
                    true
                }
            }
            exerciseList.addView(exerciseText)
        }
    }

    private fun saveWorkoutDays() {
        val editor = sharedPreferences.edit()
        val daysMap = workoutDays.mapValues { (_, layout) ->
            (0 until layout.childCount).map { (layout.getChildAt(it) as? TextView)?.text.toString() }
        }
        editor.putString("workoutDays", gson.toJson(daysMap))
        editor.apply()
    }

    private fun loadWorkoutDays() {
        val json = sharedPreferences.getString("workoutDays", "")
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            val daysMap: Map<String, List<String>> = gson.fromJson(json, type)
            daysMap.forEach { (day, exercises) ->
                addWorkoutDay(day)
                addExercisesToDay(day, exercises)
            }
        }
    }

    private fun deleteWorkoutDay(dayName: String) {
        workoutDays.remove(dayName)
        binding.layoutWorkoutDays.removeAllViews()
        workoutDays.keys.forEach { addWorkoutDay(it) }
        saveWorkoutDays()
    }

    private fun animateDelete(view: View, onAnimationEnd: () -> Unit) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha)
        animator.duration = 300
        animator.interpolator = AccelerateInterpolator()
        animator.start()
        animator.doOnEnd { onAnimationEnd() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
