package com.example.fitnesstracker.Login_SignUp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivitySignUpBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.Show_Toast
import com.example.fitnesstracker.setup_pages.check_Gmail
import com.example.fitnesstracker.setup_pages.check_Password
import com.example.fitnesstracker.setup_pages.check_Password_Error

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Users")

        binding.signupButton.setOnClickListener {
            val intenttext = Intent(this, SignUp::class.java)
            startActivity(intenttext)
        }

        binding.signupButton.setOnClickListener {
            val userName = binding.usernamelEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.editText2.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && userName.isNotEmpty() && check_Password(
                    password
                ) && check_Gmail(email)
            ) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val user = SharedPrefHelper.User(userName, email, password, 0, "", 0, 0)

                        db.collection("users").add(user).addOnSuccessListener {
                            it.update("id", it.id)
                        }
                        auth.currentUser!!.sendEmailVerification()
                        Snackbar.make(
                            binding.root,
                            "please verify your email check our email",
                            Snackbar.LENGTH_LONG
                        ).show()

                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        sendVerificationEmail()
                        startActivity(Intent(this, LogIn::class.java))
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                when {
                    userName.isEmpty() -> Show_Toast(this, "Please fill in Full Name Field")
                    email.isEmpty() -> Show_Toast(this, "Please fill in Email Field")
                    password.isEmpty() -> Show_Toast(this, "Please fill in Password Field")
                    confirmPassword.isEmpty() -> Show_Toast(
                        this,
                        "Please fill in Confirm Password Field",

                        )

                    !check_Password(password) -> Show_Toast(
                        this, check_Password_Error(password)
                    )

                    else -> Show_Toast(this, "The email must be @gmail.com")
                }

                    }
                }

         }
    fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent! Please check your email.", Toast.LENGTH_LONG).show()

                // Redirect to login screen after sign-up
                startActivity(Intent(this, LogIn::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to send verification email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }}}
}