package com.example.fitnesstracker.NavigationApp

import WorkoutExpandableListAdapter
import WorkoutViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitnesstracker.R

class WorkoutPlansFragment : Fragment() {

    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_workout_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseList: Map<String, List<Any>> = mapOf(
            "PPL" to listOf(
               "Push" to  listOf( // Push Workout
                    "\nBench Press (Barbell) – 3x8-12",
                    "\nIncline Press (Dumbbell) – 3x8-12",
                    "\nSeated Shoulder Press (Dumbbell) – 3x8-12",
                    "\nLateral Raise (Dumbbell) – 3x12-15",
                    "\nSkullcrushers (Dumbbell) – 3x10-15",
                    "\nTricep Rope Pushdown – 3x12-20"
                ),
                "Pull" to listOf(
                    "\nPull Up or Chin Up – 3x6-12",
                    "\nBent Over Row (Barbell) – 3x6-12",
                    "\nSeated Cable Row (Wide Grip) – 3x10-15",
                    "\nDumbbell Row – 2x10-15",
                    "\nBicep Curl (Barbell) – 3x8-12",
                    "\nPreacher Curl (Machine) – 2x12-20",
                    "\nFace Pull – 2-3x12-25"
                ),
                "Legs" to listOf(
                    "\nSquat (Barbell) – 3x6-10",
                    "\nRomanian Deadlift (Barbell) – 3x6-10",
                    "\nBulgarian Split Squat – 3x8-15",
                    "\nGlute Ham Raise – 3x10-15",
                    "\nStanding Calf Raise (Machine) – 3x8-15"
                )
            ),


            "Workout A" to listOf(
                listOf(
                    "\n Squats – 3x6-8",
                    "\nBench Press – 3x6-8",
                    "\nPull-Ups or Lat Pull-Downs – 3x8-10",
                    "\nShoulder Press – 3x8-10",
                    "\nLeg Curls – 3x8-10",
                    "\nBiceps Curls – 3x10-15",
                    "\nFace Pulls – 3x10-15"
                )
            ),
            "Workout B" to listOf(
                listOf(
                    "\nRomanian Deadlift – 3x6-8",
                    "\nSeated Cable Rows – 3x6-8",
                    "\nIncline Dumbbell Press – 3x8-10",
                    "\nLeg Press or Split Squats – 3x10-12",
                    "\nLateral Raises – 3x10-15",
                    "\nTriceps Pushdowns – 3x10-15",
                    "\nStanding Calf Raises – 4x6-10"
                )
            ),
            "Workout Schedule" to listOf(
                listOf(
                    "\nDay 1: Chest and Back",
                    "\nDay 2: Shoulders, Upper Arms, and Forearms",
                    "\nDay 3: Quads, Hamstring, Calves, and Lower Back",
                    "\nDay 4: Chest and Back",
                    "\nDay 5: Shoulders, Upper Arms, and Forearms",
                    "\nDay 6: Quads, Hamstring, Calves, and Lower Back",
                    "\nDay 7: Rest"
                )
            )
        )

        val expandableListView: ExpandableListView = view.findViewById(R.id.expandableListView)

        val categories = exerciseList.keys.toList()
        val exercises = exerciseList.values.toList()

        val adapter =
            context?.let { WorkoutExpandableListAdapter(it, categories, exercises, workoutViewModel) }
        expandableListView.setAdapter(adapter)

        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val selectedCategory = categories[groupPosition]
            val selectedExercise = exercises[groupPosition][childPosition]

            workoutViewModel.addWorkout(selectedCategory, listOf(selectedExercise))
            Toast.makeText(requireContext(), "Added: $selectedExercise", Toast.LENGTH_SHORT).show()

            true
        }
    }
}