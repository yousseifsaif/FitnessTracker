package com.example.fitnesstracker.toast

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.fragment.app.Fragment

fun updateOrientationLock(activity: Activity) {
    val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("AppSettings", MODE_PRIVATE)
    val isLockScreen = sharedPreferences.getBoolean("LockScreen", false)

    if (isLockScreen) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

fun updateOrientationLock(fragment: Fragment) {
    val sharedPreferences: SharedPreferences =
        fragment.requireActivity().getSharedPreferences("AppSettings", MODE_PRIVATE)
    val isLockScreen = sharedPreferences.getBoolean("LockScreen", false)

    if (isLockScreen) {
        fragment.requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
        fragment.requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}