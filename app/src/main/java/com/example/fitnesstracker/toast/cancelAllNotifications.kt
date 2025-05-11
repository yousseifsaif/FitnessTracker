import android.app.NotificationManager
import android.content.Context

// Assuming you have a NotificationManager instance
fun cancelAllNotifications(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Cancel all notifications from the app
    notificationManager.cancelAll()
}
