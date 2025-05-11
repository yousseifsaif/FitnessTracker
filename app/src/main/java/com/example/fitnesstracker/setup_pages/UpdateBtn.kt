package com.example.fitnesstracker.setup_pages

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.graphics.toColorInt

fun updateButtons(buttons: List<Button>, selectedButton: Button?) {
    buttons.forEach { button ->
        if (button == selectedButton) {
            button.setTextColor(Color.WHITE)
            button.backgroundTintList = ColorStateList.valueOf("#896CFE".toColorInt())
        } else {
            resetButton(button)
        }
    }
}

fun resetButton(button: Button) {
    button.setTextColor("#896cfe".toColorInt())
    button.backgroundTintList = ColorStateList.valueOf("#FFFFFFFF".toColorInt())
}