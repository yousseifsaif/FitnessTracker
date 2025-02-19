package com.example.fitnesstracker.NavigationApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.setup_pages.saveLoginState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


        var fab = findViewById<FloatingActionButton>(R.id.fab)

        saveLoginState(this, false)


        fab.setOnClickListener { intent = Intent(this, HomeActivity::class.java) }
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.running -> {
                    intent = Intent(this, RuningFragment::class.java)
                    startActivity(intent)
                    true
                }

                R.id.meals -> {
                    intent = Intent(this, MealsFragment::class.java)
                    startActivity(intent)
                    true
                }

                R.id.people -> {
                    intent = Intent(this, ComunicationFragment::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    intent = Intent(this, ProfileFragment::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }

        }
    }

}

