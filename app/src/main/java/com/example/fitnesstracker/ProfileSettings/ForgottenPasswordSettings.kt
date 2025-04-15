package com.example.fitnesstracker.ProfileSettings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.NavigationApp.FragmentProfile
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityForgottenPasswordSettingsBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.updateUserField
import com.example.fitnesstracker.toast.showToast

class ForgottenPasswordSettings : AppCompatActivity() {
    private lateinit var binding: ActivityForgottenPasswordSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgottenPasswordSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.confirmButtonResetButton.setOnClickListener {
            editPasswordSetting(binding.confirmNewPasswordEditText.text.toString().trim())
        }
    }

    private fun editPasswordSetting(password : String ) {
        when {
            binding.passwordEditText.text.isEmpty() ->
                showToast(this, "Please enter your current password", Toast.LENGTH_SHORT)


            binding.newPasswordEditText.text.isEmpty() ->
                showToast(this, "Please enter new password", Toast.LENGTH_SHORT)


            binding.confirmNewPasswordEditText.text.isEmpty() ->
                showToast(this, "Please confirm your new password", Toast.LENGTH_SHORT)


            binding.newPasswordEditText.text.toString() != binding.confirmNewPasswordEditText.text.toString() ->
                showToast(this, "The confirm password doesn't match", Toast.LENGTH_SHORT)


            else -> {
                val sharedPrefHelper = SharedPrefHelper(this)
                updateUserField("password" , password ,sharedPrefHelper.getUserFromPrefs().id)
                sharedPrefHelper.updateUserFieldPref("password" , password)
                val intent = Intent(this , FragmentProfile::class.java)
                startActivity(intent)
                finish()
                showToast(this, "Password successfully changed", Toast.LENGTH_SHORT)

            }
        }
    }
}
