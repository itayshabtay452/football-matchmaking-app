package com.example.soccergamesfinder.viewmodel

import android.content.Intent
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


    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
        }
    }

    fun checkIfUserExists() {
        viewModelScope.launch {
            _userExists.value = userRepository.hasUserData()
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.createUser(user)
            _user.value = user
        }
    }

    fun logout() {
        _userExists.value = null
        _user.value = null
    }

    fun getId(): String? {
        return userRepository.getUserId()
    }
}