package com.example.fitnesstracker.Login_SignUp

import ButtonClickUtil
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.databinding.LoginBinding
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.getNextActivity
import com.example.fitnesstracker.setup_pages.getUserId
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.setup_pages.saveLoginState
import com.example.fitnesstracker.setup_pages.updateUserField
import com.example.fitnesstracker.toast.shakeView
import com.example.fitnesstracker.toast.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.forgetPassword.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

                startActivity(Intent(this, ForgottenPassword::class.java))
            }
        }

        binding.signupText.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

                startActivity(Intent(this, SignUp::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        binding.loginButton.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

                validationLogin()
            }
        }
    }

    private fun validationLogin() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = "Please enter your email"
            shakeView(binding.emailEditText)
            shakeView(binding.text1)
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Please enter your password"
            shakeView(binding.passwordEditText)
            shakeView(binding.text2)

        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        binding.loading.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    getUserId(email) { doc ->
                        if (doc.isNotEmpty()) {
                            db.collection("users").document(doc).get()
                                .addOnSuccessListener { document ->

                                    val userData =
                                        document.toObject(SharedPrefHelper.User::class.java)
                                            ?: SharedPrefHelper.User()
                                    updateUserField("password", password, userData.id)
                                    SharedPrefHelper(this).updateUserFieldPref("password", password)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        SharedPrefHelper(this).saveUserToPrefs(userData)

                                        val nextActivity = getNextActivity(userData)
                                        startActivity(
                                            nav(
                                                NavData(
                                                    nextActivity, this, userData.id
                                                )
                                            )
                                        )
                                        saveLoginState(this, true)
                                        finish()


                                    }, 500)
                                }.addOnFailureListener { e ->
                                    Log.e("fireStore", "Error fetching user data", e)
                                    runOnUiThread {
                                        showToast(
                                            this, "Error fetching user data"
                                        )
                                        binding.loading.visibility = View.GONE

                                    }
                                }
                        } else {
                            Log.e("fireStore", "Error: User ID is empty!")
                            runOnUiThread {
                                showToast(this, "User ID not found")
                                binding.loading.visibility = View.GONE

                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        user?.sendEmailVerification()
                        showToast(this, "Please verify your email")
                        binding.loading.visibility = View.GONE

                    }
                }
            } else {
                runOnUiThread {
                    showToast(this, "Invalid email or password!")
                    binding.loading.visibility = View.GONE
                }
            }
        }
    }
}