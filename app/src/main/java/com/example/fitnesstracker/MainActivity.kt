package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.NavigationApp.HomeFragment
import com.example.fitnesstracker.NavigationApp.ProfileFragment
import com.example.fitnesstracker.NavigationApp.WorkoutsFragment
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.setup_pages.saveLoginState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.workouts -> replaceFragment(WorkoutsFragment())
               // R.id.meals -> replaceFragment(())

                R.id.profile -> replaceFragment(ProfileFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}