// GameDetailsViewModel.kt
package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for game actions like joining, leaving, and deleting games.
 */

data class GameDetailsState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())
    val state: StateFlow<GameDetailsState> = _state.asStateFlow()

    fun joinGame(game: Game, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                showError("User not authenticated")
                return@launch
            }
            val result = gameRepository.joinGame(game.id, userId)
            if (result.isSuccess) {
                _state.value = _state.value.copy(isLoading = false, error = null)
                onSuccess()
            } else {
                showError("הצטרפות למשחק נכשלה")
            }
        }
    }

    fun leaveGame(game: Game, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = gameRepository.leaveGame(game.id)
            if (result.isSuccess) {
                _state.value = _state.value.copy(isLoading = false, error = null)
                onSuccess()
            } else {
                showError("עזיבת המשחק נכשלה")
            }
        }
    }

    fun deleteGame(game: Game, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = gameRepository.deleteGame(game.id)
            if (result.isSuccess) {
                _state.value = _state.value.copy(isLoading = false, error = null)
                onSuccess()
            } else {
                showError("מחיקת המשחק נכשלה")
            }
        }
    }

    fun createGame(game: Game, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = gameRepository.createGame(game)

            if (result.isSuccess) {
                _state.value = _state.value.copy(isLoading = false)
                onResult(true)
            } else {
                _state.value = _state.value.copy(isLoading = false, error = "שגיאה ביצירת המשחק")
                onResult(false)
            }
        }
    }


    private fun showError(message: String) {
        _state.value = _state.value.copy(isLoading = false, error = message)
    }
}
