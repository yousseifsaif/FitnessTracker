package com.example.fitnesstracker.Login_SignUp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.setup_pages.SetupPage
import com.google.firebase.auth.FirebaseAuth


class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        val text=findViewById<TextView>(R.id.text)
        val emailInput = findViewById<EditText>(R.id.EmailEditText)
        val passwordInput = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordInput = findViewById<EditText>(R.id.editText2)
        val mobileInput = findViewById<EditText>(R.id.mobileEditText)
        val registerButton = findViewById<Button>(R.id.signup_button)
text.setOnClickListener{
 var   intentText=Intent(this,LogIn::class.java)
    startActivity(intentText)
    finish()
}

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()
            val mobile = mobileInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT)
                                .show()
                            intent = Intent(this, SetupPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed: ${task.exception?.message}",
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
