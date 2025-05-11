package com.example.fitnesstracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnesstracker.setup_pages.SharedPrefHelper

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<SharedPrefHelper.User>()
    val user: LiveData<SharedPrefHelper.User> get() = _user

    fun updateUser(newUser: SharedPrefHelper.User) {
        Log.d("com.example.fitnesstracker.viewmodel.UserViewModel", "Updating user: $newUser")
        _user.postValue(newUser)
    }
}