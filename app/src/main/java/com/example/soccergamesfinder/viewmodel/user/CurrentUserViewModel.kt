package com.example.soccergamesfinder.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentUserState())
    val state: StateFlow<CurrentUserState> = _state.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    fun setLoggedIn(value: Boolean) {
        _isUserLoggedIn.value = value
        if (value) {
            loadCurrentUser()
        } else {
            _state.value = CurrentUserState() // אפס את כל הנתונים
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                _state.update { it.copy(isLoading = false, error = "User not authenticated") }
                return@launch
            }

            val user = userRepository.getUserById(userId)
            if (user != null) {
                _state.update { it.copy(user = user, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, error = "User not found") }
            }
        }
    }

    fun signOut() {
        userRepository.signOut()
        _isUserLoggedIn.value = false
        _state.value = CurrentUserState()
    }
}
