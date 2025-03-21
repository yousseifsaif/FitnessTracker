package com.example.fitnesstracker.Login_SignUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.databinding.LoginBinding
import com.example.fitnesstracker.setup_pages.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: LoginBinding  // استخدام نفس المتغير في كل مكان

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // زر نسيان كلمة المرور
        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgottenPassword::class.java))
        }

        // زر الانتقال إلى شاشة التسجيل
        binding.signupText.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        // زر تسجيل الدخول
        binding.loginButton.setOnClickListener {
            validationLogin()
        }
    }

    private fun validationLogin() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = "Please enter your email"
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Please enter your password"
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        getUserId(email) { doc ->
                            if (doc.isNotEmpty()) {
                                db.collection("users").document(doc)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        val userData = document.toObject(SharedPrefHelper.User::class.java) ?: SharedPrefHelper.User()
                                        Log.d("LoginTest", userData.toString())

                                        SharedPrefHelper(this).saveUserToPrefs(userData)

                                        val nextActivity = getNextActivity(userData)
                                        if (nextActivity != null) {
                                            startActivity(
                                                nav(
                                                    NavData(
                                                        nextActivity,
                                                        this,
                                                        userData.id
                                                    )
                                                )
                                            )
                                            saveLoginState(this, true)
                                            finish()
                                        } else {
                                            Log.e("LoginTest", "Error: Next activity is null!")
                                            runOnUiThread {
                                                Toast.makeText(this, "Error: Could not determine next screen", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("fireStore", "Error fetching user data", e)
                                        runOnUiThread {
                                            Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Log.e("fireStore", "Error: User ID is empty!")
                                runOnUiThread {
                                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
