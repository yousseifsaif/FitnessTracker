package com.example.fitnesstracker.ToolBarIcons.Nottifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun sendMessage(msg: String) {
        _message.value = msg
    }
}
