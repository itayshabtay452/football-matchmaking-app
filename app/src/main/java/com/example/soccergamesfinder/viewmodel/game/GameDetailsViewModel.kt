// GameViewModel.kt
package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.repository.FieldRepository
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
    private val userRepository: UserRepository,
    private val fieldRepository: FieldRepository
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

            val deleteResult = gameRepository.deleteGame(game.id)
            if (deleteResult.isFailure) {
                _state.value = _state.value.copy(isLoading = false)
                showError("מחיקת המשחק נכשלה")
                return@launch
            }

            val removeFromFieldResult = fieldRepository.removeGameFromField(
                fieldId = game.fieldId,
                gameId = game.id
            )

            _state.value = _state.value.copy(isLoading = false)

            if (removeFromFieldResult.isFailure) {
                showError("המשחק נמחק, אך לא הוסר מהמגרש")
            } else {
                onSuccess()
            }
        }
    }


    fun createGameAndAttach(game: Game, onResult: (Boolean) -> Unit) {
        println(">>> התחלה: יצירת המשחק בפועל ב־Firestore")
        viewModelScope.launch {
            val result = gameRepository.createGameAndAttachToField(game)
            if (result.isSuccess) {
                onResult(result.isSuccess)
            } else {
                println(">>> שמירת המשחק נכשלה: ${result.exceptionOrNull()?.message}")
            }
        }
    }




    private fun showError(message: String) {
        _state.value = _state.value.copy(isLoading = false, error = message)
    }
}
