package com.example.fitnesstracker.Login_SignUp

import ButtonClickUtil
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivitySignUpBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.check_Gmail
import com.example.fitnesstracker.setup_pages.check_Password
import com.example.fitnesstracker.setup_pages.check_Password_Error
import com.example.fitnesstracker.toast.showToast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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

        binding.text.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

            val intentText = Intent(this, LogIn::class.java)
            startActivity(intentText)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
            }
        }

        binding.signupButton.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

                val userName = binding.usernamelEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                val confirmPassword = binding.editText2.text.toString().trim()
                val email = binding.emailEditText.text.toString().trim()

                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@preventSpamClick
                }

                if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && userName.isNotEmpty() && check_Password(
                        password
                    ) && check_Gmail(email)
                ) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                val user =
                                    SharedPrefHelper.User(userName, email, password, 0, "", 0, 0)

                                db.collection("users").add(user).addOnSuccessListener {
                                    it.update("id", it.id)
                                }
                                auth.currentUser!!.sendEmailVerification()
                                Snackbar.make(
                                    binding.root,
                                    "please verify your email check our email",
                                    Snackbar.LENGTH_LONG
                                ).show()

                                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT)
                                    .show()
                                sendVerificationEmail()
                                startActivity(Intent(this, LogIn::class.java))
                            } else {
                                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    when {
                        userName.isEmpty() -> showToast(this, "Please fill in Full Name Field")
                        email.isEmpty() -> showToast(this, "Please fill in Email Field")
                        password.isEmpty() -> showToast(this, "Please fill in Password Field")
                        confirmPassword.isEmpty() -> showToast(
                            this,
                            "Please fill in Confirm Password Field",
                        )

                        !check_Password(password) -> showToast(
                            this, check_Password_Error(password)
                        )

                        else -> showToast(this, "The email must be @gmail.com")
                    }

                }
            }
        }
    }

    private fun sendVerificationEmail() {
        val user = auth.currentUser

        user!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Verification link has been sent to your email", Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this, LogIn::class.java))
                finish()
            }
        }
    }
}