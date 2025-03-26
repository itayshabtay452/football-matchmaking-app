package com.example.soccergamesfinder.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.AuthRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository // ⬅️ חדש
) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.getCurrentUser())
    val user: StateFlow<FirebaseUser?> get() = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _userExists = MutableStateFlow<Boolean?>(null)
    val userExists: StateFlow<Boolean?> get() = _userExists


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loggedUser = authRepository.login(email, password)
            if (loggedUser != null) {
                _user.value = loggedUser
            } else {
                _errorMessage.value = "התחברות נכשלה"
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val newUser = authRepository.register(email, password)
            if (newUser != null){
                _user.value = newUser
            } else {
                _errorMessage.value = "הרשמות נכשלה"
            }
        }
    }

    fun getGoogleSignInIntent(): Intent {
        return authRepository.getGoogleSignInIntent()
    }

    fun handleGoogleSignInResult(data: Intent?) {

        viewModelScope.launch {
            val googleUser = authRepository.signInWithGoogle(data)
            if (googleUser != null) {
                _user.value = googleUser
            } else {
                _errorMessage.value = "הרשמות גוגל נכשלה"
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _user.value = null
        _errorMessage.value = null
        _userExists.value = null
    }

    fun checkIfUserExists() {
        viewModelScope.launch {
            _userExists.value = userRepository.hasUserData()
        }
    }


}
