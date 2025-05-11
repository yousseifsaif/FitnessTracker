package com.example.fitnesstracker.ToolBarIcons

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.fitnesstracker.databinding.DialogBreakTimerBinding

class BreakTimerDialog(private val listener: BreakTimerListener) : DialogFragment() {

    private var _binding: DialogBreakTimerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogBreakTimerBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        // Disable notification sound
        //  setNotificationSoundNull(requireContext())

        binding.btnStartTimer.setOnClickListener {
            val totalTimeStr = binding.etTotalTime.text.toString()

            if (totalTimeStr.isNotEmpty()) {
                val totalTime = totalTimeStr.toLong() * 60 * 1000
                listener.onStartTimer(totalTime)
                dismiss()
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    // Function to disable notification sound
//    private fun setNotificationSoundNull(context: Context) {
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = "break_timer_channel"  // Keep this as val since it's not reassigned
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(
//                channelId,
//                "Break Timer Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                sound = null
//            }
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("Timer Started")
//            .setContentText("Your break timer has started.")
//            .setSmallIcon(android.R.drawable.ic_notification_overlay)
//            .build()
//
//        notificationManager.notify(1, notification)
//    }

    interface BreakTimerListener {
        fun onStartTimer(totalTime: Long)
    }
}