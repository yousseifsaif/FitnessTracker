package com.example.fitnesstracker.NavigationApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitnesstracker.databinding.FragmentProfileBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.viewmodel.UserViewModel  // ✅ FIXED IMPORT

class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()  // ✅ Correct ViewModel usage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            binding.tvUserName.text = updatedUser.name ?: "N/A"
            binding.tvAge.text = updatedUser.age?.toString() ?: "N/A"
            binding.tvUserEmail.text = updatedUser.email ?: "N/A"
            binding.tvHeight.text = updatedUser.height?.toString() ?: "N/A"
            binding.tvWeight.text = updatedUser.weight?.toString() ?: "N/A"
        }

        // Button Click Listener
        binding.btnPrfoile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // ✅ Prevent memory leaks
    }
}
