package com.example.fitnesstracker.setup_pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserData(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: Int = 0,
    val gender: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val Id: String = ""
)

fun fetchUsersData(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").get()
        .addOnSuccessListener { documents ->
            onSuccess(documents)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun checkUserExists(
    email: String,
    context: Context,
    callback: (String) -> Unit
) {
    if (email.isEmpty()) {
        showToast("Please enter your email in the field", context)
        callback("Email is empty")
    }
    val db = FirebaseFirestore.getInstance()
    db.collection("users").whereEqualTo("email", email).get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                showToast("this doesn't have an account", context)
                callback("No user found")
            } else {
                callback("the email is right")
            }
        }
        .addOnFailureListener { exception ->
            showToast("Failed to check user: ${exception.message}", context)
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
