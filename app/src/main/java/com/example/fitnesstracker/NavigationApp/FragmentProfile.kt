package com.example.fitnesstracker.NavigationApp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitnesstracker.Login_SignUp.LogIn
import com.example.fitnesstracker.NavigationApp.ProfileFields.EditProfile
import com.example.fitnesstracker.NavigationApp.ProfileFields.HelpActivity
import com.example.fitnesstracker.NavigationApp.ProfileFields.PrivacyPolicy
import com.example.fitnesstracker.NavigationApp.ProfileFields.SettingsActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.DialogLogoutBinding
import com.example.fitnesstracker.databinding.FragmentProfileBinding
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.viewmodel.UserViewModel

class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()  // ✅ Correct ViewModel usage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val sharedPref = SharedPrefHelper(requireContext())
        val userData = sharedPref.getUserFromPrefs()


        if (userData == null) {
            Log.e("FragmentProfile", "User data is null!")
        } else {
            Log.d("FragmentProfile", "User data retrieved: $userData")
            userViewModel.updateUser(userData)  // ✅ Updating ViewModel only if userData exists
        }

        // Observe LiveData
        userViewModel.user.observe(viewLifecycleOwner) { updatedUser ->
            binding.tvUserName.text = updatedUser.name
            binding.tvAge.text = updatedUser.age.toString()
            binding.tvUserEmail.text = updatedUser.email
            binding.tvHeight.text = updatedUser.height.toString()
            binding.tvWeight.text = updatedUser.weight.toString()
        }
        binding.btnLogout.setOnClickListener {
            showCustomDialog()

        }
        // Button Click Listener
        binding.btnProfile.setOnClickListener {

            val intent = nav(NavData(EditProfile::class.java, requireContext(), id.toString()))
            intent.putExtra("id", id)
            startActivity(intent)
        }


        binding.btnPolicy.setOnClickListener {
            val intent = nav(NavData(PrivacyPolicy::class.java, requireContext(), id.toString()))
            startActivity(intent)

        }
        binding.btnSettings.setOnClickListener {
            val intent = nav(NavData(SettingsActivity::class.java, requireContext(), id.toString()))
            startActivity(intent)
        }
        binding.btnHelp.setOnClickListener {
            val intent = nav(NavData(HelpActivity::class.java, requireContext(), id.toString()))


            startActivity(intent)
        }


        return binding.root
    }

    fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val dialogBinding = DialogLogoutBinding.bind(dialogView) // ✅ استخدم الـ Binding الصحيح

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView)
            .setCancelable(false) // Prevent dismiss on outside touch
            .create()
        dialog.setContentView(dialogView)
        dialogBinding.btnYes.setOnClickListener {
            logoutUser(requireContext())
            Toast.makeText(requireContext(), "User Logged out!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 🔥 إزالة الخلفية الافتراضية
        dialog.show()
    }

    private fun logoutUser(context: Context) {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()  // 🔥 استخدم `apply()` بدلاً من `commit()` لتحسين الأداء

//        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireActivity(), LogIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


}
