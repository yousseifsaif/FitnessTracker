package com.example.fitnesstracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fitnesstracker.NavigationApp.FragmentProfile
import com.example.fitnesstracker.NavigationApp.HomeFragment
import com.example.fitnesstracker.NavigationApp.MealsFragment
import com.example.fitnesstracker.NavigationApp.WorkoutsFragment
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import com.example.fitnesstracker.ToolBarIcons.AppDatabase
import com.example.fitnesstracker.ToolBarIcons.BreakTimerDialog
import com.example.fitnesstracker.ToolBarIcons.Nottifications.CongratsWorker
import com.example.fitnesstracker.ToolBarIcons.Nottifications.DailyNotificationWorker
import com.example.fitnesstracker.ToolBarIcons.Nottifications.NotificationActivity
import com.example.fitnesstracker.ToolBarIcons.SearchFragment
import com.example.fitnesstracker.databinding.ActivityMainBinding
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.toast.updateOrientationLock
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), BreakTimerDialog.BreakTimerListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0
    private var isFabOpen = false
    private val NOTIFICATION_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        requestNotificationPermission()
        saveOpenTime(this)
        scheduleDailyNotification()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        initializeDatabase(this)
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
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()

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
                        500, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )


                val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.alamsound)
                mediaPlayer.start()
                sharedPreferences.edit().remove("TIMER_END_TIME").apply()
            }
        }.start()
    }

    fun initializeDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(context)
            if (db.searchDao().getExerciseCount() == 0) {
                val sampleExercises = listOf(
                    ExerciseEntity(
                        name = "Push Up",
                        bodyPart = "Chest",
                        equipment = "Body Weight",
                        target = "Pectorals",
                        gifUrl = "",
                        secondaryMuscles = "Triceps,Shoulders",
                        instructions = "1. Place hands...\n2. Lower body..."
                    ), ExerciseEntity(
                        name = "Squat",
                        bodyPart = "Legs",
                        equipment = "Body Weight",
                        target = "Quadriceps",
                        gifUrl = "",
                        secondaryMuscles = "Hamstrings,Glutes",
                        instructions = "1. Stand straight...\n2. Bend knees..."
                    )
                )
                db.searchDao().insertAllExercises(sampleExercises)
            }
        }
    }

    fun scheduleDailyNotification() {
        val dailyRequest =
            PeriodicWorkRequestBuilder<DailyNotificationWorker>(1, TimeUnit.DAYS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "daily_notification", ExistingPeriodicWorkPolicy.KEEP, dailyRequest
        )
    }

    fun saveOpenTime(context: Context) {
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_opened_time", System.currentTimeMillis()).apply()

        // ✨ جدولة نوتيفيكيشن التهنئة بعد ساعة
        val request =
            OneTimeWorkRequestBuilder<CongratsWorker>().setInitialDelay(1, TimeUnit.HOURS).build()

        WorkManager.getInstance(context).enqueue(request)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateOrientationLock(this)
    }
}