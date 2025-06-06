// GameViewModel.kt
package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.model.Notification
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.NotificationsRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val fieldRepository: FieldRepository,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())
    val state: StateFlow<GameDetailsState> = _state.asStateFlow()

    fun joinGame(game: Game, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val userId = userRepository.getCurrentUserId() ?: return@launch showError("User not authenticated")
            val user = userRepository.getUserById(userId) ?: return@launch showError("User not found")

            val result = gameRepository.joinGame(game.id, userId)
            if (result.isSuccess) {
                userRepository.followGame(userId, game.id)
                sendNotificationToFollowersOfGame(
                    game,
                    senderId = userId,
                    message = "${user.nickname} הצטרף למשחק ב-${game.fieldName}"
                )
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
            val userId = userRepository.getCurrentUserId() ?: return@launch showError("User not authenticated")
            val user = userRepository.getUserById(userId) ?: return@launch showError("User not found")

            val result = gameRepository.leaveGame(game.id)
            if (result.isSuccess) {
                userRepository.unfollowGame(userId, game.id)
                sendNotificationToFollowersOfGame(
                    game,
                    senderId = userId,
                    message = "${user.nickname} עזב את המשחק ב-${game.fieldName}"
                )
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
            val userId = userRepository.getCurrentUserId() ?: return@launch showError("User not authenticated")
            val user = userRepository.getUserById(userId) ?: return@launch showError("User not found")

            val deleteResult = gameRepository.deleteGame(game.id)
            if (deleteResult.isFailure) {
                _state.value = _state.value.copy(isLoading = false)
                showError("מחיקת המשחק נכשלה")
                return@launch
            }

            fieldRepository.removeGameFromField(game.fieldId, game.id)
            userRepository.removeGameFromAllUsers(game.id)

            sendNotificationToFollowersOfGame(
                game,
                senderId = userId,
                message = "${user.nickname} מחק את המשחק ב-${game.fieldName}"
            )

            _state.value = _state.value.copy(isLoading = false)
            onSuccess()
        }
    }

    fun createGameAndAttach(game: Game, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = gameRepository.createGameAndAttachToField(game)
            if (result.isSuccess) {
                val userId = userRepository.getCurrentUserId()
                if (userId != null) {
                    userRepository.followGame(userId, game.id)
                    val user = userRepository.getUserById(userId)
                    if (user != null) {
                        sendNotificationToFollowersOfField(
                            fieldId = game.fieldId,
                            senderId = userId,
                            message = "${user.nickname} יצר משחק חדש במגרש ${game.fieldName}"
                        )
                    }
                }
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    private suspend fun sendNotificationToFollowersOfGame(game: Game, senderId: String, message: String) {
        val followers = userRepository.getUsersFollowingGame(game.id)
        for (userId in followers) {
            if (userId == senderId) continue
            notificationsRepository.addNotification(
                userId,
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "עדכון במשחק",
                    message = message,
                    timestamp = Timestamp.now(),
                    targetId = game.id,
                    type = "game",
                    isRead = false
                )
            )
        }
    }

    private suspend fun sendNotificationToFollowersOfField(fieldId: String, senderId: String, message: String) {
        val followers = userRepository.getUsersFollowingField(fieldId)
        for (userId in followers) {
            if (userId == senderId) continue
            notificationsRepository.addNotification(
                userId,
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "משחק חדש!",
                    message = message,
                    timestamp = Timestamp.now(),
                    targetId = fieldId,
                    type = "field",
                    isRead = false
                )
            )
        }
    }

    private fun showError(message: String) {
        _state.value = _state.value.copy(isLoading = false, error = message)
    }
}

data class GameDetailsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
