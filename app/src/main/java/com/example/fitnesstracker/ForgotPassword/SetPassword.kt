package com.example.fitnesstracker.ForgotPassword

import android.content.Intent
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivitySetPasswordBinding
import com.example.fitnesstracker.setup_pages.Show_Toast
import com.example.fitnesstracker.setup_pages.check_Password_Error
import com.google.firebase.firestore.FirebaseFirestore

class SetPassword : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var document: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()

        val intent = intent
        document = intent.getStringExtra("id").toString()

        enableEdgeToEdge()
        val binding = ActivitySetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.resetPasswordButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.cPassword.text.toString().trim()

            if (password.isNotEmpty() && password == confirmPassword) {
                if(check_Password_Error(password) == "Password is valid"){
                    val userRef = db.collection("users").document(document)
                    userRef.update("password", password)
                        .addOnSuccessListener {
                            Show_Toast(this,"Password updated successfully")
                            startActivity(Intent(this, LogIn::class.java))
                        }
                        .addOnFailureListener { exception ->
                            Show_Toast(this,"$exception")
                        }
                }else
                    Show_Toast(this,check_Password_Error(password))

            } else {
                Show_Toast(this,"Passwords do not match")
            }
        }
    }


}
