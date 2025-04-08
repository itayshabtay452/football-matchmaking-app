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

    init {
        loadCurrentUser()
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
}
