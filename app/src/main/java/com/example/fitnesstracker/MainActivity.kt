package com.example.fitnesstracker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.NavigationApp.FragmentProfile
import com.example.fitnesstracker.NavigationApp.HomeFragment
import com.example.fitnesstracker.NavigationApp.MealsFragment
import com.example.fitnesstracker.NavigationApp.WorkoutsFragment
import com.example.fitnesstracker.ToolBarIcons.BreakTimerDialog
import com.example.fitnesstracker.ToolBarIcons.NotificationFragment
import com.example.fitnesstracker.ToolBarIcons.SearchFragment
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), BreakTimerDialog.BreakTimerListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        getUserName()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        val endTime = sharedPreferences.getLong("TIMER_END_TIME", 0)
        if (endTime > SystemClock.elapsedRealtime()) {
            remainingTime = endTime - SystemClock.elapsedRealtime()
            startBreakTimer(remainingTime)
        }

        binding.ivTimer.setOnClickListener {
            val dialog = BreakTimerDialog(this)
            dialog.show(supportFragmentManager, "BreakTimerDialog")
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

        val isProfileFragment = fragment is FragmentProfile
        binding.apply {
            tvGreeting.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
            ivSearch.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
            ivNotifications.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
            tvSubtext.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
            ivTimer.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
            tvTimerMain.visibility = if (isProfileFragment) View.GONE else View.VISIBLE
        }
    }

    private fun getUserName() {
        val sharedPrefHelper = SharedPrefHelper(this)
        val user = sharedPrefHelper.getUserFromPrefs()
        binding.tvGreeting.text = if (user.name.isNotEmpty()) {
            "Hi, ${user.name}"
        } else {
            Log.e("MainActivity", "not found")
            "Hi, User!"
        }
    }

    override fun onStartTimer(totalTime: Long) {
        startBreakTimer(totalTime)
    }

    private fun startBreakTimer(totalTime: Long) {
        binding.tvTimerMain.visibility = View.VISIBLE
        val endTime = SystemClock.elapsedRealtime() + totalTime
        sharedPreferences.edit().putLong("TIMER_END_TIME", endTime).apply()
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimerMain.text = String.format("%02d:%02d", minutes, seconds)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                binding.tvTimerMain.text = "00:00"
                binding.tvTimerMain.visibility = View.GONE

                val vibrator = getSystemService(Vibrator::class.java)
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

                val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.alamsound)
                mediaPlayer.start()
                sharedPreferences.edit().remove("TIMER_END_TIME").apply()
            }
        }.start()
    }
}
