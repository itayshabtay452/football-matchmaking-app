// CreateGameViewModel.kt
package com.example.soccergamesfinder.ui.screens.creategame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.utils.GameValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateGameFormState())
    val state: StateFlow<CreateGameFormState> = _state.asStateFlow()

    fun onEvent(
        event: CreateGameEvent,
        field: Field,
        onSubmit: (Game) -> Unit
    ) {
        when (event) {
            is CreateGameEvent.DateSelected -> {
                _state.update { it.copy(date = event.value) }
            }

            is CreateGameEvent.StartTimeSelected -> {
                _state.update { it.copy(startTime = event.value) }
            }

            is CreateGameEvent.EndTimeSelected -> {
                _state.update { it.copy(endTime = event.value) }
            }

            is CreateGameEvent.MaxPlayersChanged -> {
                _state.update { it.copy(maxPlayers = event.value) }
            }

            is CreateGameEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.value) }
            }

            is CreateGameEvent.Cancel -> {
                _state.value = CreateGameFormState()
            }

            is CreateGameEvent.Submit -> {
                submit(field, onSubmit)
            }
        }
    }

    private fun submit(
        field: Field,
        onSubmit: (Game) -> Unit
    ) {
        val current = _state.value

        // שלב 1 – ולידציה בסיסית
        val basicValidations = listOf(
            GameValidationUtils.validateDate(current.date),
            GameValidationUtils.validateStartTime(current.startTime),
            GameValidationUtils.validateEndTime(current.startTime, current.endTime),
            GameValidationUtils.validateMaxPlayers(current.maxPlayers)
        )

        val firstError = basicValidations.firstOrNull { !it.isValid }
        if (firstError != null) {
            showError(firstError.errorMessage ?: "שגיאה לא ידועה")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val creatorId = userRepository.getCurrentUserId()
                if (creatorId == null) {
                    showError("המשתמש לא מחובר")
                    return@launch
                }

                // המרת תאריך + שעת התחלה + שעת סיום ל־Timestamp
                val start = GameValidationUtils.convertToTimestamp(current.date, current.startTime)
                val end = GameValidationUtils.convertToTimestamp(current.date, current.endTime)

                // שליפת משחקים מהמאגרים – של המגרש ושל המשתמש
                val fieldGames = gameRepository.getGamesByFieldId(field.id)
                val userGames = gameRepository.getGamesForCurrentUser()

                // ולידציית זמינות מגרש
                val fieldResult = GameValidationUtils.validateFieldAvailability(start, end, fieldGames)
                if (!fieldResult.isValid) {
                    showError(fieldResult.errorMessage!!)
                    return@launch
                }

                // ולידציית זמינות משתמש
                val userResult = GameValidationUtils.validateUserAvailability(creatorId, start, end, userGames)
                if (!userResult.isValid) {
                    showError(userResult.errorMessage!!)
                    return@launch
                }

                // יצירת המשחק
                val game = Game(
                    id = UUID.randomUUID().toString(),
                    fieldId = field.id,
                    creatorId = creatorId,
                    joinedPlayers = listOf(creatorId),
                    maxPlayers = current.maxPlayers,
                    description = current.description,
                    startTime = start,
                    endTime = end,
                    fieldName = field.name,
                    fieldAddress = field.address,
                    creatorName = userRepository.getUserById(creatorId)?.nickname ?: "לא ידוע"
                )

                onSubmit(game)
                _state.value = CreateGameFormState() // איפוס
            } catch (e: Exception) {
                e.printStackTrace()
                showError("שגיאה בעת יצירת המשחק")
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    private fun showError(message: String) {
        _state.update { it.copy(isLoading = false, error = message) }
    }
}
