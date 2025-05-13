package com.example.fitnesstracker.NavigationApp.ProfileFields

import ButtonClickUtil
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.ProfileSettings.ForgottenPasswordSettings
import com.example.fitnesstracker.ProfileSettings.NotificationsSettings
import com.example.fitnesstracker.databinding.ActivitySettingsAcitvityBinding
import com.example.fitnesstracker.databinding.DialogLogoutBinding
import com.example.fitnesstracker.toast.updateOrientationLock
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsAcitvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.switchDarkMode.isChecked = isDarkModeEnabled(this)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkModeEnabled(this, isChecked)
            restartApp()
        }

        binding.forgetPassword.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                startActivity(Intent(this, ForgottenPasswordSettings::class.java))
            }
        }

        binding.deleteBtn.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                showDeleteConfirmationDialog()
            }
        }

        binding.notificationSettingBtn.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent = Intent(this, NotificationsSettings::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBinding = DialogLogoutBinding.inflate(LayoutInflater.from(this))
        val dialog =
            android.app.AlertDialog.Builder(this).setView(dialogBinding.root).setCancelable(false)
                .create()

        dialogBinding.btnYes.setOnClickListener {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPref.getString("id", null)
            val user = FirebaseAuth.getInstance().currentUser

            if (userId != null && user != null) {
                deleteDocument("users", userId)

                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPref.edit { clear() }
                        Toast.makeText(this, "Okay, we will miss you! 😢", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LogIn::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Log.e("FirebaseDelete", "Deletion failed", task.exception)
                        if (task.exception is FirebaseAuthRecentLoginRequiredException) {
                            Toast.makeText(
                                this,
                                "Please re-authenticate before deleting your account.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error deleting account. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()
    }

    private fun deleteDocument(collection: String, documentId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection(collection).document(documentId).delete().addOnSuccessListener {
            Log.d("Firestore", "DocumentSnapshot successfully deleted!")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error deleting document", e)
        }
    }

    private fun setDarkModeEnabled(context: Context, isEnabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("dark_mode", isEnabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun isDarkModeEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("dark_mode", false)
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}
