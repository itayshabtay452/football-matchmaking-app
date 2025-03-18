package com.example.soccergamesfinder.viewmodel

import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _userExists = MutableStateFlow<Boolean?>(null)
    val userExists: StateFlow<Boolean?> get() = _userExists

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> get() = _userId


    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
            _userId.value = getId()
        }
    }

    fun checkIfUserExists() {
        viewModelScope.launch {
            _userExists.value = userRepository.hasUserData()
        }
    }

    fun saveUser(name: String,nickname: String, age: Int?, city: String, latitude: Double?, longitude: Double?) {
        viewModelScope.launch {
            val userProfile = age?.let { User(it, city, name, nickname,latitude, longitude) }
            if (userProfile != null) {
                userRepository.createUser(userProfile)
                _user.value = userProfile
            }
        }
    }

    fun logout() {
        _userExists.value = null
        _user.value = null
    }

    private fun getId(): String? {
        return userRepository.getUserId()
    }
}