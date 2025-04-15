package com.example.fitnesstracker.setup_pages

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore




fun checkUserExists(
    email: String,
    context: Context,
    callback: (String) -> Unit
) {
    if (email.isEmpty()) {
        Show_Toast(context, "Please enter your email in the field")
        callback("Email is empty")
    }
    val db = FirebaseFirestore.getInstance()
    db.collection("users").whereEqualTo("email", email).get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                Show_Toast(context, "this doesn't have an account")
                callback("No user found")
            } else {
                callback("the email is right")
            }
        }
        .addOnFailureListener { exception ->
            Show_Toast(context, "Failed to check user: ${exception.message}")
        }

}


fun getUserId(email: String, callback: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                callback("")
            } else {
                val documentId = documents.documents.first().id
                Log.d("Firestorm", "Document ID: $documentId")
                callback(documentId)
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestorm", "Error getting document: ", exception)
            callback("")
        }

}

fun Show_Toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun check_Gmail(email: String): Boolean {
    var index = 0
    for (i in 0 until email.length) if (email[i] == '@') {
        index = i
        break
    }

    return email.drop(index) == "@gmail.com"
}

fun check_Password(password: String): Boolean {
    if (password.length >= 6) {
        for (i in 0 until password.length) {
            val codeUnit: Char = password[i]
            if ((codeUnit >= '!' && codeUnit <= '&') || codeUnit == '@') return true
        }
    }
    return false
}

fun check_Password_Error(password: String): String {
    if (password.length <= 6) return "The password should be longer than 6 characters"

    var flag = false
    val specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~"

    for (i in password.indices) {
        if (i < password.length - 1 && password[i] == password[i + 1]) return "You cannot use same two characters"

        if (password[i] in specialChars) flag = true

    }
    return if (flag) "Password is valid"
    else "The password must contain at least one special character"
}

fun getUserField(documentId: String = "", fieldName: String, callback: (Any?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(documentId).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val fieldValue = document.getString(fieldName)
                callback(fieldValue)
            } else {
                callback(null)
            }
        }
        .addOnFailureListener {
            callback(null)
        }
}

fun updateUserField(key: String, value: Any, id: String) {
    val db = Firebase.firestore
    db.collection("users").document(id).update(key, value)
        .addOnSuccessListener { Log.d("Age", "DocumentSnapshot successfully written!") }
        .addOnSuccessListener { Log.d("ID", id) }

}


fun getNextActivity(data: SharedPrefHelper.User): Class<out AppCompatActivity> {
    return when {
        data.gender.isEmpty() -> Gender::class.java
        data.age == 0 -> AgeActivity::class.java
        data.height == 0 -> HeightActivitySelection::class.java
        data.weight == 0 -> WeightActivitySelection::class.java
        data.selectedGoal.isEmpty() -> GoalActivity::class.java
        data.ActivityLevel.isEmpty() -> ActivityLevel::class.java
        else -> MainActivity::class.java
    }
}


fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isLoggedIn", isLoggedIn)
    editor.apply()
}



class SharedPrefHelper(context: Context) {

    val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    data class User(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val age: Int = 0,
        val gender: String = "",
        val height: Int = 0,
        val weight: Int = 0,
        val id: String = "",
        val selectedGoal: String = "",
        val weighttype: String = "",
        val calories: Int = 0,
        val ActivityLevel: String = "",
        val isLoggedIn: Boolean = false
    )

    fun getUserFromPrefs(): User {
        val id = prefs.getString("id", "")

        Log.d("SharedPrefHelper", "Retrieved ID: $id")

        if (id.isNullOrEmpty()) {
            Log.e("SharedPrefHelper", "Error: No user data found!")
            return User()
        }

        return User(
            name = prefs.getString("name", "") ?: "",
            email = prefs.getString("email", "") ?: "",
            password = prefs.getString("password", "") ?: "",
            age = prefs.getInt("age", 0),
            gender = prefs.getString("gender", "") ?: "",
            height = prefs.getInt("height", 0),
            weight = prefs.getInt("weight", 0),
            id = id,
            selectedGoal = prefs.getString("selectedGoal", "") ?: "",
            weighttype = prefs.getString("weighttype", "") ?: "",
            calories = prefs.getInt("calories", 0),
            ActivityLevel = prefs.getString("ActivityLevel", "") ?: "",
            isLoggedIn = prefs.getBoolean("isLoggedIn", false)
        )
    }



    fun saveUserToPrefs(user: User) {
        prefs.edit().apply {
            putString("name", user.name)
            putString("email", user.email)
            putString("password", user.password)
            putInt("age", user.age)
            putString("gender", user.gender)
            putInt("height", user.height)
            putInt("weight", user.weight)
            putString("id", user.id)
            putString("selectedGoal", user.selectedGoal)
            putString("weighttype", user.weighttype)
            putInt("calories", user.calories)
            putString("ActivityLevel", user.ActivityLevel)
            putBoolean("isLoggedIn", user.isLoggedIn)
            apply()
        }
    }
    fun updateUserFieldPref(key: String, value: Any) {
        val editor = prefs.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value::class.java}")
        }
        editor.apply()
    }
}


fun calculateCal(data: SharedPrefHelper.User): Double {
    var weight: Double;
    weight = data.weight.toDouble()
    if (data.weighttype == "lb") weight = data.weight / 2.20462;

    var bmr: Double
    if (data.gender == "women") {
        bmr = 655.1 + (9.563 * weight) + (1.850 * data.height) - (4.676 * data.age)
        if (data.ActivityLevel == "beginner") bmr *= 1.2
        else if (data.ActivityLevel == "intermediate") bmr *= 1.4
        else if (data.ActivityLevel == "advance") bmr *= 1.6


        if (data.selectedGoal == "lose") return bmr - 500
        else if (data.selectedGoal == "gain") return bmr + 500
        else if (data.selectedGoal == "massGain") return bmr + 1000
        else if (data.selectedGoal == "shapeBody") return bmr + 250
        else (0.0).toDouble()

    } else {
        bmr = 66.47 + (13.75 * weight) + (5.003 * data.height) - (6.755 * data.age)

        if (data.ActivityLevel == "beginner") bmr *= 1.3
        else if (data.ActivityLevel == "intermediate") bmr *= 1.5
        else if (data.ActivityLevel == "advance") bmr *= 1.7

        if (data.selectedGoal == "lose") return bmr - 500
        else if (data.selectedGoal == "gain") return bmr + 500
        else if (data.selectedGoal == "massGain") return bmr + 1500
        else if (data.selectedGoal == "shapeBody") return bmr + 500
        else (0.0).toDouble()
    }
    return 0.0
}

data class NavData(
    val nav: Class<out AppCompatActivity>,
    val context: Context,
    val id: String
)

fun nav(data: NavData): Intent {
    val nextIntent = Intent(data.context, data.nav)
    nextIntent.putExtra("id", data.id)
    return nextIntent
}
