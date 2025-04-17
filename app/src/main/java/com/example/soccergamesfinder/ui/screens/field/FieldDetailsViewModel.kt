package com.example.soccergamesfinder.ui.screens.field

import androidx.lifecycle.ViewModel
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.google.firebase.firestore.ListenerRegistration

@HiltViewModel
class FieldDetailsViewModel @Inject constructor(
    private val fieldRepository: FieldRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FieldDetailsState())
    val state: StateFlow<FieldDetailsState> = _state.asStateFlow()

    private var fieldListener: ListenerRegistration? = null
    private var gamesListener: ListenerRegistration? = null

    fun startListening(fieldId: String) {
        // מאזין לשינויים במגרש
        fieldListener?.remove()
        fieldListener = fieldRepository.listenToFieldById(
            fieldId = fieldId,
            onChange = { field ->
                _state.update { it.copy(field = field, isLoading = false, error = null) }
            },
            onError = { e ->
                _state.update { it.copy(error = e.message ?: "שגיאה בטעינת המגרש", isLoading = false) }
            }
        )

        // מאזין לשינויים במשחקים של המגרש
        gamesListener?.remove()
        gamesListener = gameRepository.listenToGamesByFieldId(
            fieldId = fieldId,
            onChange = { games ->
                _state.update { it.copy(games = games) }
                updateStatistics(games)
            },
            onError = { e ->
                _state.update { it.copy(error = e.message ?: "שגיאה בטעינת המשחקים") }
            }
        )
    }

    private fun updateStatistics(games: List<Game>) {
        val total = games.size
        val open = games.count { it.status == GameStatus.OPEN }
        val full = games.count { it.joinedPlayers.size >= it.maxPlayers }
        val avg = if (games.isNotEmpty()) {
            games.sumOf { it.joinedPlayers.size }.toDouble() / games.size
        } else 0.0

        _state.update {
            it.copy(
                totalGames = total,
                openGames = open,
                fullGames = full,
                avgPlayers = avg
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        fieldListener?.remove()
        gamesListener?.remove()
    }
}
