package com.example.fitnesstracker.ProfileSettings

import android.content.Context

data class NotificationSettings(
    var generalNotification: Boolean = false,
    var sound: Boolean = false,
    var dontDisturbMode: Boolean = false,
    var vibrate: Boolean = false,
    var lockScreen: Boolean = false,
    var reminders: Boolean = false
) {
    companion object {
        private const val PREF_NAME = "AppSettings"

        fun load(context: Context): NotificationSettings {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return NotificationSettings(
                generalNotification = prefs.getBoolean("generalNotification", false),
                sound = prefs.getBoolean("Sound", false),
                dontDisturbMode = prefs.getBoolean("dontDisturbMode", false),
                vibrate = prefs.getBoolean("Vibrate", false),
                lockScreen = prefs.getBoolean("LockScreen", false),
                reminders = prefs.getBoolean("Reminders", false)
            )
        }

        fun save(context: Context, settings: NotificationSettings) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean("generalNotification", settings.generalNotification)
            editor.putBoolean("Sound", settings.sound)
            editor.putBoolean("dontDisturbMode", settings.dontDisturbMode)
            editor.putBoolean("Vibrate", settings.vibrate)
            editor.putBoolean("LockScreen", settings.lockScreen)
            editor.putBoolean("Reminders", settings.reminders)
            editor.apply()
        }
    }
}