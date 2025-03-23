package com.example.fitnesstracker.ForgotPassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.databinding.ActivityForgottenPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgottenPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgottenPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgottenPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.resetPasswordBtn.setOnClickListener {
            val email = binding.editText.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                showSnackbar("Please enter an email", binding.root)
            }
        }
    }

    private fun resetPassword(email: String) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "Link Send Success!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, LogIn::class.java))
//                        finish()


                } else {
                    showSnackbar("Failed to send reset email", binding.root, "Retry") {
                        resetPassword(email)
                    }
                }
            }
    }

    private fun showSnackbar(
        message: String,
        view: View,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        actionText?.let {
            snackbar.setAction(it) { action?.invoke() }
        }
        snackbar.show()
    }
}
