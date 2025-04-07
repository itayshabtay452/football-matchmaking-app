package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameListState())
    val state: StateFlow<GameListState> = _state.asStateFlow()

    init {
        loadAllGames()
    }

    /**
     * Loads all games.
     */
    private fun loadAllGames() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val games = gameRepository.getAllGames()
            _state.update {
                it.copy(isLoading = false, games = games)
            }
        }
    }

    /**
     * Loads games for a specific field.
     */
    fun loadGamesByField(fieldId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val games = gameRepository.getGamesByFieldId(fieldId)
            _state.update {
                it.copy(isLoading = false, games = games)
            }
        }
    }

    /**
     * Loads games for the current user.
     */
    fun loadGamesForCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val games = gameRepository.getGamesForCurrentUser()
            _state.update {
                it.copy(isLoading = false, games = games)
            }
        }
    }

    fun updateSingleGame(gameId: String) {
        viewModelScope.launch {
            val updatedGame = gameRepository.getGameById(gameId)
            if (updatedGame != null) {
                val currentGames = _state.value.games.toMutableList()
                val index = currentGames.indexOfFirst { it.id == gameId }
                if (index != -1) {
                    currentGames[index] = updatedGame
                    _state.update { it.copy(games = currentGames) }
                }
            }
        }
    }

}
