package com.example.fitnesstracker

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.NavigationApp.FragmentProfile
import com.example.fitnesstracker.NavigationApp.HomeFragment
import com.example.fitnesstracker.NavigationApp.MealsFragment
import com.example.fitnesstracker.NavigationApp.WorkoutsFragment
import com.example.fitnesstracker.ToolBarIcons.NotificationFragment
import com.example.fitnesstracker.ToolBarIcons.SearchFragment
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.saveLoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        getUserName()
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
        binding.ivSearch.setOnClickListener {
            replaceFragment(SearchFragment())
        }
        binding.ivNotifications.setOnClickListener {
            replaceFragment(NotificationFragment())
        }



        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.workouts -> replaceFragment(WorkoutsFragment())
                R.id.meals -> replaceFragment(MealsFragment())

                R.id.profile -> replaceFragment(FragmentProfile())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun getUserName() {
        val sharedPrefHelper = SharedPrefHelper(this)
        val user = sharedPrefHelper.getUserFromPrefs()
        if (user.name.isNotEmpty()) {
            binding.tvGreeting.text = user.name
            binding.tvGreeting.text = "Hi, ${user.name}"
        } else {
            val sharedPrefHelper = SharedPrefHelper(this)
            val localUser = sharedPrefHelper.getUserFromPrefs()
            if (localUser.name.isNotEmpty()) {
                binding.tvGreeting.text = "Hi, ${localUser.name}"
            } else {
                Log.e("MainActivity", "not found")
                binding.tvGreeting.text = "Hi, User!"  //
            }
        }
    }
    }

