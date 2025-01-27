package com.example.fitnesstracker.setup_pages

import RulerAdapterHeight
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class HeightActivitySelection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_height_selection)

        val heightDisplay = findViewById<TextView>(R.id.heightDisplay)
        val rulerRecyclerView = findViewById<RecyclerView>(R.id.rulerRecyclerView)
        val continueButton = findViewById<TextView>(R.id.continueButton)

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
                val position = (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2
                heightDisplay.text = heights[position].toString()
            }
        })

        // Handle continue button click
        continueButton.setOnClickListener {

            val selectedHeight = heightDisplay.text.toString()
            Toast.makeText(this, "Selected Height: $selectedHeight cm", Toast.LENGTH_SHORT).show()
            // Navigate to the next page or save height
            intent=Intent(this,WeightActivitySelection::class.java)
startActivity(intent)
        }
    }
}
