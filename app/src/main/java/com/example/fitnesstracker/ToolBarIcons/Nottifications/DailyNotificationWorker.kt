package com.example.fitnesstracker.ToolBarIcons.Nottifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyNotificationWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        val sharedPref = applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val lastOpened = sharedPref.getLong("last_opened_time", 0)
        val now = System.currentTimeMillis()

        // فرق يوم تقريبًا (24 ساعة = 86400000 ملي ثانية)
        if (now - lastOpened > 86400000) {
            NotificationUtils.showNotification(
                applicationContext, "رجعنا ليك!", "مشتاقينلك! افتح التطبيق وكمّل تمرينك اليوم."
            )
        }
        return Result.success()
    }
}
