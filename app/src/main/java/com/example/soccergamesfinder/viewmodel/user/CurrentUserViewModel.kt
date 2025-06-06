package com.example.soccergamesfinder.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.firestore.ListenerRegistration
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

    private var userListener: ListenerRegistration? = null

    fun setLoggedIn(value: Boolean) {
        _isUserLoggedIn.value = value
        if (value) {
            listenToUserRealtime()
        } else {
            _state.value = CurrentUserState()
            userListener?.remove()
            userListener = null
        }
    }

    private fun listenToUserRealtime() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                _state.update { it.copy(isLoading = false, error = "User not authenticated") }
                _isUserLoggedIn.value = false
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            userListener?.remove() // בטל האזנה ישנה אם קיימת

            userListener = userRepository.listenToUserById(
                userId = userId,
                onChange = { user ->
                    if (user != null) {
                        _state.update { it.copy(user = user, isLoading = false, error = null) }
                        _isUserLoggedIn.value = true
                    } else {
                        _state.update { it.copy(isLoading = false, error = "User not found") }
                        _isUserLoggedIn.value = false
                    }
                },
                onError = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Error loading user") }
                }
            )
        }
    }

    fun signOut() {
        userListener?.remove()
        userListener = null
        userRepository.signOut()
        _isUserLoggedIn.value = false
        _state.value = CurrentUserState()
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
    }
}
