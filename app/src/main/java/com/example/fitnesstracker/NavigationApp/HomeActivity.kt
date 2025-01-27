package com.example.fitnesstracker.NavigationApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { intent = Intent(this, HomeActivity::class.java) }
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.running -> {
                    intent = Intent(this, RunningActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.meals -> {
                    intent = Intent(this, MealsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.people -> {
                    intent = Intent(this, CommunicationActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }

        }
    }

}

