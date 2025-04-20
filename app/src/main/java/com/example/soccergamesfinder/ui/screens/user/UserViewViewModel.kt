// 📁 ui/screens/user/UserViewViewModel.kt
package com.example.soccergamesfinder.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserViewState())
    val state: StateFlow<UserViewState> = _state.asStateFlow()

    fun loadProfile(userId: String) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                if (user == null) {
                    _state.update { it.copy(isLoading = false, error = "המשתמש לא נמצא") }
                    return@launch
                }

                val games = gameRepository.getGamesForUser(userId)

                val currentUserId = userRepository.getCurrentUserId()
                val isOwnProfile = user.id == currentUserId

                _state.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        games = games,
                        isOwnProfile = isOwnProfile
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "שגיאה בטעינת הפרופיל") }
            }
        }
    }
}
