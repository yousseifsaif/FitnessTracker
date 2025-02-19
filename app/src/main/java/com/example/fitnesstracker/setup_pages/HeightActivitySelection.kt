package com.example.fitnesstracker.setup_pages

import RulerAdapterHeight
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityHeightSelectionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HeightActivitySelection : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityHeightSelectionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val heightDisplay = binding.heightDisplay
        val rulerRecyclerView = binding.rulerRecyclerView
        val continueButton = binding.continueButton

        // Set up the RecyclerView for the ruler
        val heights = (140..200).toList() // Range of heights (140-200 cm)
        val adapter = RulerAdapterHeight(heights)
        rulerRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rulerRecyclerView.layoutManager = layoutManager

        // Scroll listener to update the height display
        rulerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2
                heightDisplay.text = heights[position].toString()
            }
        })

        // Handle continue button click
        continueButton.setOnClickListener {
            val id = intent.getStringExtra("id")
            if (id != null) {
                val userHeight = heightDisplay.text.toString().toInt()
                updateUserField("height",userHeight,id)

                val editor = SharedPrefHelper(this).prefs.edit()
                editor.putString("height",userHeight.toString()).apply()
            }
            startActivity(nav(NavData(WeightActivitySelection::class.java, this, id.toString())))

        }
    }
}