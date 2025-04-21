package com.example.fitnesstracker.NavigationApp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutPlansScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            WorkoutCard(
                title = "💪 Push Workout",
                exercises = listOf(
                    "• Bench Press (Barbell) – 3x8-12",
                    "• Incline Press (Dumbbell) – 3x8-12",
                    "• Seated Shoulder Press – 3x8-12",
                    "• Lateral Raise – 3x12-15",
                    "• Skullcrushers – 3x10-15",
                    "• Tricep Rope Pushdown – 3x12-20"
                ),
                cardColor = Color(0xFFD32F2F)
            )
        }
        item {
            WorkoutCard(
                title = "🧲 Pull Workout",
                exercises = listOf(
                    "• Pull Up or Chin Up – 3x6-12",
                    "• Bent Over Row – 3x6-12",
                    "• Seated Cable Row – 3x10-15",
                    "• Dumbbell Row – 2x10-15",
                    "• Bicep Curl – 3x8-12",
                    "• Preacher Curl – 2x12-20",
                    "• Face Pull – 2-3x12-25"
                ),
                cardColor = Color(0xFF1976D2)
            )
        }
        item {
            WorkoutCard(
                title = "🦵 Leg Workout",
                exercises = listOf(
                    "• Squat – 3x6-10",
                    "• Romanian Deadlift – 3x6-10",
                    "• Bulgarian Split Squat – 3x8-15",
                    "• Glute Ham Raise – 3x10-15",
                    "• Standing Calf Raise – 3x8-15"
                ),
                cardColor = Color(0xFF388E3C)
            )
        }
        item {
            WorkoutCard(
                title = "🔥 Workout A",
                exercises = listOf(
                    "• Squats – 3x6-8",
                    "• Bench Press – 3x6-8",
                    "• Pull-Ups – 3x8-10",
                    "• Shoulder Press – 3x8-10",
                    "• Leg Curls – 3x8-10",
                    "• Biceps Curls – 3x10-15",
                    "• Face Pulls – 3x10-15"
                ),
                cardColor = Color(0xFFF57C00)
            )
        }
        item {
            WorkoutCard(
                title = "🔥 Workout B",
                exercises = listOf(
                    "• Romanian Deadlift – 3x6-8",
                    "• Seated Cable Rows – 3x6-8",
                    "• Incline Dumbbell Press – 3x8-10",
                    "• Leg Press – 3x10-12",
                    "• Lateral Raises – 3x10-15",
                    "• Triceps Pushdowns – 3x10-15",
                    "• Standing Calf Raises – 4x6-10"
                ),
                cardColor = Color(0xFF512DA8)
            )
        }
        item {
            WorkoutCard(
                title = "📅 Workout Schedule",
                exercises = listOf(
                    "• Day 1: Chest and Back",
                    "• Day 2: Shoulders, Arms , Forearms",
                    "• Day 3: Legs , Lower Back",
                    "• Day 4: Chest and Back",
                    "• Day 5: Shoulders, Arms , Forearms",
                    "• Day 6: Legs , Lower Back",
                    "• Day 7: Rest 💤"
                ),
                cardColor = Color(0xFF0097A7)
            )
        }
    }
}

@Composable
fun WorkoutCard(title: String, exercises: List<String>, cardColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp), // ✅ Fix here
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            exercises.forEach { exercise ->
                Text(
                    text = exercise,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewWorkoutPlans() {
    WorkoutPlansScreen()
}
