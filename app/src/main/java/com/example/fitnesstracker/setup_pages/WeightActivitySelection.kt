package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class WeightActivitySelection : AppCompatActivity() {
    private var isKgSelected = true
    private var selectedWeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weight_selection)


        val kgButton = findViewById<Button>(R.id.kgButton)
        val lbButton = findViewById<Button>(R.id.lbButton)
        val selectedWeightText = findViewById<TextView>(R.id.selectedWeight)
        val weightPicker = findViewById<RecyclerView>(R.id.weightPicker)
        val continueButton = findViewById<Button>(R.id.continueButton)

        weightPicker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val weightAdapter = RulerAdapter(40..200) { weight ->
            selectedWeight = weight
            val unit = if (isKgSelected) "Kg" else "Lb"
            selectedWeightText.text = "$selectedWeight $unit"
        }
        weightPicker.adapter = weightAdapter

        kgButton.setOnClickListener {
            isKgSelected = true
            kgButton.setBackgroundColor(Color.BLACK)
            lbButton.setBackgroundColor(Color.DKGRAY)
            selectedWeightText.text = "$selectedWeight Kg"
        }

        lbButton.setOnClickListener {
            isKgSelected = false
            kgButton.setBackgroundColor(Color.DKGRAY)
            lbButton.setBackgroundColor(Color.BLACK)
            selectedWeightText.text = "$selectedWeight Lb"
        }

        continueButton.setOnClickListener {
            val unit = if (isKgSelected) "Kg" else "Lb"
            println("Selected Weight: $selectedWeight $unit")
            intent= Intent(this,GoalActivity::class.java)
            startActivity(intent)

        }
    }


}