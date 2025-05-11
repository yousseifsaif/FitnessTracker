package com.example.fitnesstracker.setup_pages

import ButtonClickUtil
import RulerAdapterHeight
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.databinding.ActivityHeightSelectionBinding
import com.example.fitnesstracker.toast.updateOrientationLock
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

        val heights = (140..200).toList()
        val adapter = RulerAdapterHeight(heights)
        rulerRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rulerRecyclerView.layoutManager = layoutManager

        rulerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2
                heightDisplay.text = heights[position].toString()
            }
        })

        continueButton.setOnClickListener {
            ButtonClickUtil.preventSpamClick(this) {
                val id = intent.getStringExtra("id")
                if (id != null) {
                    val userHeight = heightDisplay.text.toString().toInt()
                    updateUserField("height", userHeight, id)

                    SharedPrefHelper(this).prefs.edit { putInt("height", userHeight) }
                }
                startActivity(
                    nav(
                        NavData(
                            WeightActivitySelection::class.java, this, id.toString()
                        )
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}