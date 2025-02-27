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

        binding.btnStartTimer.setOnClickListener {
            val totalTimeStr = binding.etTotalTime.text.toString()

            if (totalTimeStr.isNotEmpty()) {
                val totalTime = totalTimeStr.toLong() * 60 * 1000 // تحويل إلى ميلي ثانية
                listener.onStartTimer(totalTime) // تمرير `totalTime` فقط
                dismiss() // إغلاق الـ Dialog
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface BreakTimerListener {
        fun onStartTimer(totalTime: Long)
    }
}
