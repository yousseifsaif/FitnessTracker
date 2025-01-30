package com.example.fitnesstracker.Login_SignUp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.databinding.ActivitySignUpBinding
import com.example.fitnesstracker.setup_pages.SetupPage
import com.example.fitnesstracker.setup_pages.UserData
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

        binding.text.setOnClickListener {
            val intenttext = Intent(this, LogIn::class.java)
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

            if (email.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && userName.isNotEmpty() &&
                check_Password(password) && check_Gmail(email)
            ) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                            val user = UserData(userName, email, password, 0, "",  0.0,  0.0)

                            db.collection("users")
                                .add(user)
                                .addOnSuccessListener {
                                    it.update("id",it.id)
                                    Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to add user: ${e.message}", Toast.LENGTH_LONG).show()
                                }

                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, SetupPage::class.java))
                        } else {
                            Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                when {
                    userName.isEmpty() -> showToast("Please fill in Full Name Field")
                    email.isEmpty() -> showToast("Please fill in Email Field")
                    password.isEmpty() -> showToast("Please fill in Password Field")
                    confirmPassword.isEmpty() -> showToast("Please fill in Confirm Password Field")
                    !check_Password(password) -> showToast(check_Password_Error(password))
                    else -> showToast("The email must be @gmail.com")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun check_Gmail(email: String): Boolean {
        var index = 0
        for (i in 0 until email.length) if (email[i] == '@') {
            index = i
            break
        }

        if (email.drop(index) == "@gmail.com")
            return true

        return false
    }

    fun check_Password(password: String): Boolean {
        if (password.length >= 6) {
            for (i in 0 until password.length) {
                val codeUnit: Char = password[i]
                if ((codeUnit >= '!' && codeUnit <= '&') || codeUnit == '@') return true
            }
        }
        return false
    }

    fun check_Password_Error(password: String): String {
        var index = 0
        if (password.length <= 6) return "The password should be longer than 6 characters"

        var flag = false
        val specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~"

        for (i in password.indices) {
            if (i < password.length - 1 && password[i] == password[i + 1])
                return "You cannot use same two characters"

            if (password[i] in specialChars)
                flag = true

        }
        return if (flag) "Password is valid"
        else "The password must contain at least one special character"
    }
}
