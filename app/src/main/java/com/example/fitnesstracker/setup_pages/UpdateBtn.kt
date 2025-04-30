package com.example.fitnesstracker.setup_pages

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button

fun updateButtons(buttons: List<Button>, selectedButton: Button?) {
    buttons.forEach { button ->
         if (button == selectedButton) {
            button.setTextColor(Color.WHITE)
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#896CFE"))
        } else {
            resetButton(button)
        }
    }
}

fun resetButton(button: Button) {
    button.setTextColor(Color.parseColor("#896cfe"))
    button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
}
