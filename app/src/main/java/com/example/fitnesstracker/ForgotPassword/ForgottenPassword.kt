package com.example.fitnesstracker.ForgotPassword

import ButtonClickUtil
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.databinding.ActivityForgottenPasswordBinding
import com.example.fitnesstracker.toast.showToast
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
            ButtonClickUtil.preventSpamClick(this) {

                val email = binding.editText.text.toString().trim()
                if (email.isNotEmpty()) {
                    resetPassword(email)
                } else {
                    showSnackBar("Please enter an email", binding.root)

                }
            }
        }
    }

    private fun resetPassword(email: String) {

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                showToast(this, "Link Send Success!")
                startActivity(Intent(this, LogIn::class.java))
                finish()


            } else {
                showSnackBar("Failed to send reset email", binding.root, "Retry") {
                    resetPassword(email)
                }
            }
        }
    }

    private fun showSnackBar(
        message: String, view: View, actionText: String? = null, action: (() -> Unit)? = null
    ) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        actionText?.let {
            snackBar.setAction(it) { action?.invoke() }
        }
        snackBar.show()
    }
}