package com.example.fitnesstracker.setup_pages

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button

fun updateButtons(buttons: List<Button>, selectedButton: Button?) {
    buttons.forEach { button ->
         if (button == selectedButton) {
            button.setTextColor(Color.parseColor("#FF000000"))
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E2F163"))
        } else {
            resetButton(button)
        }
    }
}

fun resetButton(button: Button) {
    button.setTextColor(Color.parseColor("#896cfe"))
    button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
}
