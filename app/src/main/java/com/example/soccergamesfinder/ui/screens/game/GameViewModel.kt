package com.example.soccergamesfinder.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val fieldRepository: FieldRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())
    val state: StateFlow<GameDetailsState> = _state.asStateFlow()

    private var gameListener: ListenerRegistration? = null

    fun startListening(gameId: String) {
        _state.update { it.copy(isLoading = true, error = null) }

        // מאזין למשחק
        gameListener?.remove()
        gameListener = gameRepository.listenToGameById(
            gameId = gameId,
            onChange = { game ->
                _state.update { it.copy(game = game) }
                loadRelatedData(game)
            },
            onError = { error ->
                _state.update { it.copy(isLoading = false, error = error.message ?: "שגיאה בטעינת המשחק") }
            }
        )
    }

    private fun loadRelatedData(game: Game) {
        viewModelScope.launch {
            try {
                val field = fieldRepository.getFieldById(game.fieldId)
                val creator = userRepository.getUserById(game.creatorId)
                val participants = userRepository.getUsersByIds(game.joinedPlayers)

                _state.update {
                    it.copy(
                        field = field,
                        creator = creator,
                        participants = participants,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "שגיאה בטעינת נתוני המשחק") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameListener?.remove()
    }
}
