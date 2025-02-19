package com.example.fitnesstracker.setup_pages

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.NavigationApp.HomeActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

data class UserData(
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
    val calories: Double = 0.0

)


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


fun getNextActivity(data: UserData, context: Context): Intent {
    return if (data.gender == "")
        Intent(context, Gender::class.java)
    else if (data.age == 0)
        Intent(context, AgeActivity::class.java)
    else if (data.height == 0)
        Intent(context, HeightActivitySelection::class.java)
    else if (data.weight == 0)
        Intent(context, WeightActivitySelection::class.java)
    else if (data.selectedGoal == "") {
        Log.d("select", "123457")
        Intent(context, GoalActivity::class.java)

    } else
        Intent(context, HomeActivity::class.java)

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
        val name: String,
        val age: Int,
        val height: Int,
        val weight: Int,
        val gender: String,
        val email: String,
        val password: String,
        val id: String,
    )

    fun getUserFromPrefs(): User {
        return User(
            name = prefs.getString("name", "") ?: "",
            age = prefs.getInt("age", 0),
            height = prefs.getInt("height", 0),
            weight = prefs.getInt("weight", 0),
            gender = prefs.getString("gender", "") ?: "",
            email = prefs.getString("email", "") ?: "",
            password = prefs.getString("password", "") ?: "",
            id = prefs.getString("id", "") ?: ""
        )
    }

    fun saveUserToPrefs(user: User) {
        prefs.edit().apply {
            putString("name", user.name)
            putInt("age", user.age)
            putInt("height", user.height)
            putInt("weight", user.weight)
            putString("gender", user.gender)
            putString("email", user.email)
            apply()
        }
    }
}
//For women, BMR = 655.1 + (9.563 x weight in kg) + (1.850 x height in cm) - (4.676 x age in years)
//For men, BMR = 66.47 + (13.75 x weight in kg) + (5.003 x height in cm) - (6.755 x age in years)

fun calculateCal(data: UserData): Double {
    var weight: Double;
    weight = data.weight.toDouble()
    if (data.weighttype == "lb") weight = data.weight / 2.20462;

    val bmr: Double
    if (data.gender == "women") {
        bmr = 655.1 + (9.563 * weight) + (1.850 * data.height) - (4.676 * data.age)
        if (data.selectedGoal == "lose") return bmr - 500
        else if (data.selectedGoal == "gain") return bmr + 500
        else if (data.selectedGoal == "massGain") return bmr + 1000
        else if (data.selectedGoal == "shapeBody") return bmr + 250
        else 0

    } else {
        bmr = 66.47 + (13.75 * weight) + (5.003 * data.height) - (6.755 * data.age)
        if (data.selectedGoal == "lose") return bmr - 500
        else if (data.selectedGoal == "gain") return bmr + 500
        else if (data.selectedGoal == "massGain") return bmr + 1500
        else if (data.selectedGoal == "shapeBody") return bmr + 500
        else 0.0
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
