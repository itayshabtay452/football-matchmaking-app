package com.example.soccergamesfinder.ui.screens.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FieldDetailsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FieldDetailsState())
    val state: StateFlow<FieldDetailsState> = _state.asStateFlow()

    private var lastField: Field? = null
    private var lastGames: List<Game> = emptyList()

    fun onDataChanged(fieldId: String, knownFields: List<Field>, knownGames: List<Game>) {
        val field = knownFields.firstOrNull { it.id == fieldId }
        val games = knownGames.filter { it.fieldId == fieldId }

        if (field != lastField || games != lastGames) {
            lastField = field
            lastGames = games

            if (field == null) {
                _state.update { it.copy(field = null, games = emptyList(), error = "המגרש לא נמצא") }
            } else {
                _state.update {
                    it.copy(
                        field = field,
                        games = games,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun loadAllGamesForField(fieldId: String) {
        viewModelScope.launch {
            try {
                val allGames = gameRepository.getGamesByFieldId(fieldId)
                updateStatisticsFromAllGames(allGames)
            } catch (e: Exception) {
                // טיפול בשגיאה בעתיד
            }
        }
    }

    fun addGameToAllGames(game: Game) {
        val updated = _state.value.allGames + game
        updateStatisticsFromAllGames(updated)
    }

    fun removeGameFromAllGames(gameId: String) {
        val updated = _state.value.allGames.filterNot { it.id == gameId }
        updateStatisticsFromAllGames(updated)
    }

    private fun updateStatisticsFromAllGames(games: List<Game>) {
        _state.update {
            it.copy(
                allGames = games,
                totalGames = games.size,
                avgPlayers = games.map { g -> g.joinedPlayers.size }.average().toInt(),
                fullGames = games.count { g -> g.joinedPlayers.size >= g.maxPlayers },
                openGames = games.count { g -> g.status == GameStatus.OPEN }
            )
        }
    }
}
