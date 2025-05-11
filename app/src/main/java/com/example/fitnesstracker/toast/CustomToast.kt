package com.example.fitnesstracker.toast

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.widget.Toast

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun shakeView(view: View) {
    val animator =
        ObjectAnimator.ofFloat(view, "translationX", 0f, 10f, -10f, 10f, -10f, 5f, -5f, 0f)
    animator.duration = 500
    animator.start()
}