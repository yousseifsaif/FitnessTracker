package com.example.fitnesstracker.NavigationApp

import ButtonClickUtil
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
import com.example.fitnesstracker.NavigationApp.ProfileFields.FavoritesActivity
import com.example.fitnesstracker.NavigationApp.ProfileFields.HelpActivity
import com.example.fitnesstracker.NavigationApp.ProfileFields.PrivacyPolicy
import com.example.fitnesstracker.NavigationApp.ProfileFields.SettingsActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.DialogLogoutBinding
import com.example.fitnesstracker.databinding.FragmentProfileBinding
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.toast.updateOrientationLock
import com.example.fitnesstracker.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val sharedPref = SharedPrefHelper(requireContext())
        val userData = sharedPref.getUserFromPrefs()


        Log.d("FragmentProfile", "User data retrieved: $userData")
        userViewModel.updateUser(userData)
        binding.tvUserName.text = userData.name
        binding.tvAge.text = userData.age.toString()
        binding.tvUserEmail.text = userData.email
        binding.tvHeight.text = userData.height.toString()
        binding.tvWeight.text = userData.weight.toString()
        binding.tvKcal.text = userData.calories.toString()

        userViewModel.user.observe(viewLifecycleOwner) { updatedUser ->

            binding.tvUserName.text = updatedUser.name
            binding.tvAge.text = updatedUser.age.toString()
            binding.tvUserEmail.text = updatedUser.email
            binding.tvHeight.text = updatedUser.height.toString()
            binding.tvWeight.text = updatedUser.weight.toString()

            binding.tvUserName.text = updatedUser.name
            binding.tvAge.text = updatedUser.age.toString()
            binding.tvUserEmail.text = updatedUser.email
            binding.tvHeight.text = updatedUser.height.toString()
            binding.tvWeight.text = updatedUser.weight.toString()
            binding.tvKcal.text = updatedUser.calories.toString()


        }
        binding.btnLogout.setOnClickListener {
            showCustomDialog()

        }


        binding.btnProfile.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent = nav(NavData(EditProfile::class.java, requireContext(), id.toString()))
                startActivity(intent)
            }
        }

        binding.btnFav.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent = Intent(requireContext(), FavoritesActivity::class.java)
                startActivity(intent)
            }
        }
        binding.btnPolicy.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {

                val intent =
                    nav(NavData(PrivacyPolicy::class.java, requireContext(), id.toString()))
                startActivity(intent)
            }
        }
        binding.btnSettings.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent =
                    nav(NavData(SettingsActivity::class.java, requireContext(), id.toString()))
                startActivity(intent)
            }
        }
        binding.btnHelp.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val intent = nav(NavData(HelpActivity::class.java, requireContext(), id.toString()))
                startActivity(intent)
            }
        }
        return binding.root
    }

    fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val dialogBinding = DialogLogoutBinding.bind(dialogView)


        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).setCancelable(false)

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

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun logoutUser(context: Context) {
        val sharedPref = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireActivity(), LogIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}