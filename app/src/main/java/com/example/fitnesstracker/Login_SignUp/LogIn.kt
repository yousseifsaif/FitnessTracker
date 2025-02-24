package com.example.fitnesstracker.Login_SignUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.databinding.ActivityLogBinding
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.getNextActivity
import com.example.fitnesstracker.setup_pages.getUserId
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.setup_pages.saveLoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, ForgottenPassword::class.java)
            startActivity(intent)
        }

        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            Log.d("LoginTest", "Email: $email")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            Log.d("logina12", "if-2")

                            getUserId(email) { doc ->
                                if (doc.isNotEmpty()) {
                                    Log.d("logina12", "if-1")

                                    Log.d("LoginTest", "User ID: $doc")
                                    db.collection("users").document(doc)
                                        .get()
                                        .addOnSuccessListener { document ->
                                            Log.d("LoginTest", "Firestore Data: ${document.data}")
                                            val data =
                                                document.toObject(SharedPrefHelper.User::class.java)
                                                    ?: SharedPrefHelper.User()
                                            Log.d("LoginTest", data.toString())
                                            if (data != null) {
                                                Log.d("logina12", "if2")

                                                SharedPrefHelper(this).saveUserToPrefs(data)
                                                val nextActivity = getNextActivity(data)
                                                if (nextActivity != null) {
                                                    Log.d("logina12", "if3")

                                                    startActivity(
                                                        nav(
                                                            NavData(
                                                                getNextActivity(data),
                                                                this,
                                                                data.id
                                                            )
                                                        )
                                                    )

                                                    saveLoginState(this, true)
                                                    finish()
                                                } else {
                                                    Log.e(
                                                        "LoginTest",
                                                        "Error: Next activity is null!"
                                                    )
                                                }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("fireStore", "Error fetching user data", e)
                                        }
                                } else {
                                    Log.e("fireStore", "Error: User ID is empty!")
                                }
                            }
                        } else {
                            Log.d("logina12", "if6")

                            Toast.makeText(
                                this,
                                "Login Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
