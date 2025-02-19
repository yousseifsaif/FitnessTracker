package com.example.fitnesstracker.ForgotPassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.Login_SignUp.SignUp
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityForgottenPasswordBinding
import com.example.fitnesstracker.setup_pages.checkUserExists
import com.example.fitnesstracker.setup_pages.getUserId
import com.google.firebase.auth.FirebaseAuth

class ForgottenPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgottenPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgottenPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        binding.resetPasswordBtn.setOnClickListener {
            val emailReset = binding.editText.text.toString().trim()

            if (emailReset.isEmpty()) {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkUserExists(emailReset, this) { result ->
                if (result == "the email is right") {
                    getUserId(emailReset) { userId ->
                        if (userId != null) {
                            val intentText = Intent(this, SetPassword::class.java)
                            intentText.putExtra("id", userId)
                            startActivity(intentText)
                        } else
                            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(this, "Email not found or invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}