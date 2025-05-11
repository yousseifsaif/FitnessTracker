package com.example.fitnesstracker.ToolBarIcons.Nottifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class CongratsWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationUtils.showNotification(
            applicationContext, "عمل ممتاز!", "👏 أنت بتقوم بشغل رائع، كمل كده!"
        )
        return Result.success()
    }
}