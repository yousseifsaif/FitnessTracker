package com.example.fitnesstracker.ToolBarIcons.Nottifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fitnesstracker.NavigationApp.apiWorkouts.AppDatabase
import com.example.fitnesstracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NotificationUtils {
    fun showNotification(context: Context, title: String, message: String) {
        val channelId = "daily_channel_id"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Daily Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification) // تأكد إنك ضايف أيقونة هنا
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
    fun notifyAndSave(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification) // لازم يكون عندك أيقونة هنا
            .setContentTitle(title)
            .setContentText(message)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())

        // 📝 حفظ في Room
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(context)
            val dao = db.notificationDao()
            dao.insertNotification(NotificationEntity(title = title, message = message))
        }
    }
}

