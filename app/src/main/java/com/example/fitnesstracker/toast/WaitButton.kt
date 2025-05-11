import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

object ButtonClickUtil {
    private var lastClickTime: Long = 0

    fun preventSpamClick(context: Context, callback: () -> Unit) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime < 1000) {
            Toast.makeText(context, "Please wait before clicking again", Toast.LENGTH_SHORT).show()
        } else {
            lastClickTime = currentTime
            callback()
        }
    }

    fun preventSpamClick(fragment: Fragment, callback: () -> Unit) {
        val context = fragment.context ?: return

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime < 1000) {
            Toast.makeText(context, "Please wait before clicking again", Toast.LENGTH_SHORT).show()
        } else {
            lastClickTime = currentTime
            callback()
        }
    }
}