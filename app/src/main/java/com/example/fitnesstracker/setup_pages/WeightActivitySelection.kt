package com.example.fitnesstracker.setup_pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.databinding.ActivityWeightSelectionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WeightActivitySelection : AppCompatActivity() {
    val db = Firebase.firestore
    private var isKgSelected = true
    private var selectedWeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityWeightSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val kgButton = binding.kgButton
        val lbButton = binding.lbButton
        val selectedWeightText = binding.selectedWeight
        val weightPicker = binding.weightPicker
        val continueButton = binding.continueButton

        weightPicker.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
            val id = intent.getStringExtra("id")

            if (!id.isNullOrEmpty()) {

                updateUserField("weight", selectedWeight, id)
                updateUserField("weighttype", unit, id)


                val editor = SharedPrefHelper(this).prefs.edit()
                editor.putString("weight", selectedWeight.toString()).apply()

                startActivity(nav(NavData(GoalActivity::class.java, this, id.toString())))

            }


        }
    }


}