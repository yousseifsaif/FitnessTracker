package com.example.fitnesstracker.toast

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String, duration: Int) {
    Toast.makeText(context, message, duration).show()
}
