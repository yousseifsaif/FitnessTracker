package com.example.fitnesstracker.Login_SignUp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.NavigationApp.HomeActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityLogBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    lateinit var editeText: EditText
    lateinit var button: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        var etEmail = findViewById<EditText>(R.id.emailEditText)
        var etPassword = findViewById<EditText>(R.id.passwordEditText)
       var forgotPassword=findViewById<TextView>(R.id.forgetPassword)
        binding.loginButton.setOnClickListener {
            intent = Intent(this, ForgottenPassword::class.java)
        startActivity(intent)
        }
        button = findViewById(R.id.login_button)
        var textLogIn = findViewById<TextView>(R.id.signup_text)

        textLogIn.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }



        button.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            saveLoginState(this, true)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }
}