package com.example.fitnesstracker.ProfileSettings

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityForgottenPasswordSettingsBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.updateUserField
import com.example.fitnesstracker.toast.showToast
import com.example.fitnesstracker.toast.updateOrientationLock
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ForgottenPasswordSettings : AppCompatActivity() {

    private lateinit var binding: ActivityForgottenPasswordSettingsBinding

    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    @SuppressLint("ClickableViewAccessibility")
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

        binding.backButton.setOnClickListener { finish() }

        binding.confirmButtonResetButton.setOnClickListener { editPasswordSetting() }

        binding.passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.passwordEditText.compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchAreaStart =
                        binding.passwordEditText.width - drawableWidth - binding.passwordEditText.paddingEnd
                    if (event.x >= touchAreaStart) {
                        isCurrentPasswordVisible = !isCurrentPasswordVisible
                        togglePasswordVisibility(binding.passwordEditText, isCurrentPasswordVisible)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.newPasswordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.newPasswordEditText.compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchAreaStart =
                        binding.newPasswordEditText.width - drawableWidth - binding.newPasswordEditText.paddingEnd
                    if (event.x >= touchAreaStart) {
                        isNewPasswordVisible = !isNewPasswordVisible
                        togglePasswordVisibility(binding.newPasswordEditText, isNewPasswordVisible)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.confirmNewPasswordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.confirmNewPasswordEditText.compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchAreaStart =
                        binding.confirmNewPasswordEditText.width - drawableWidth - binding.confirmNewPasswordEditText.paddingEnd
                    if (event.x >= touchAreaStart) {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                        togglePasswordVisibility(
                            binding.confirmNewPasswordEditText, isConfirmPasswordVisible
                        )
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility(editText: android.widget.EditText, isVisible: Boolean) {
        val selection = editText.selectionEnd
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.twotone_visibility_24, 0
            )
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.twotone_visibility_off_24, 0
            )
        }
        editText.setSelection(selection)
    }

    private fun editPasswordSetting() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val currentPassword = binding.passwordEditText.text.toString()
        val newPasswordText = binding.newPasswordEditText.text.toString()
        val confirmPassword = binding.confirmNewPasswordEditText.text.toString()

        if (currentPassword.isEmpty()) {
            showToast(this, "Please enter your current password")
            return
        }

        if (newPasswordText.isEmpty()) {
            showToast(this, "Please enter a new password")
            return
        }

        if (confirmPassword.isEmpty()) {
            showToast(this, "Please confirm your new password")
            return
        }

        if (newPasswordText != confirmPassword) {
            showToast(this, "The confirm password doesn't match")
            return
        }

        if (user == null) {
            showToast(this, "User not logged in")
            return
        }

        val sharedPrefHelper = SharedPrefHelper(this)
        val userData = sharedPrefHelper.getUserFromPrefs()
        val credential = EmailAuthProvider.getCredential(userData.email, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                user.updatePassword(newPasswordText).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        updateUserField("password", newPasswordText, userData.id)
                        sharedPrefHelper.updateUserFieldPref("password", newPasswordText)
                        showToast(this, "Password successfully changed")
                        finish()
                    } else {
                        showToast(
                            this,
                            "Failed to update password: ${updateTask.exception?.message}",
                            Toast.LENGTH_LONG
                        )
                    }
                }
            } else {
                showToast(
                    this, "Re-authentication failed: ${authTask.exception?.message}"
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}