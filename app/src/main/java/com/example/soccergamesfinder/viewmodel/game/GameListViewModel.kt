package com.example.soccergamesfinder.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
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

            val now = com.google.firebase.Timestamp.now()

            val updatedGames = games.map { game ->
                val newStatus = when {
                    game.endTime < now -> GameStatus.ENDED
                    game.joinedPlayers.size >= game.maxPlayers -> GameStatus.FULL
                    else -> GameStatus.OPEN
                }

                if (game.status != newStatus) {
                    // נעדכן את הסטטוס בפיירסטור
                    launch {
                        gameRepository.updateGameStatus(game.id, newStatus)
                    }
                }

                game.copy(status = newStatus)
            }

            val openGames = updatedGames
                .filter { it.status != GameStatus.ENDED }
                .sortedBy { it.startTime }

            _state.update {
                it.copy(isLoading = false, games = openGames)
            }
        }
    }


    fun updateSingleGame(gameId: String) {
        viewModelScope.launch {
            val updatedGame = gameRepository.getGameById(gameId) ?: return@launch

            val isNowFull = updatedGame.joinedPlayers.size >= updatedGame.maxPlayers
            if (isNowFull && updatedGame.status != GameStatus.FULL) {
                gameRepository.updateGameStatus(updatedGame.id, GameStatus.FULL)
            }

            val updated = if (isNowFull) updatedGame.copy(status = GameStatus.FULL) else updatedGame

            val currentGames = _state.value.games.toMutableList()
            val index = currentGames.indexOfFirst { it.id == gameId }

            if (index != -1) {
                currentGames[index] = updated
                _state.update { it.copy(games = currentGames) }
            }
        }
    }


    fun addGame(game: Game) {
        _state.update { current ->
            current.copy(games = listOf(game) + current.games)
        }
    }

    fun removeGame(gameId: String) {
        _state.update { current ->
            val updatedGames = current.games.filterNot { it.id == gameId }
            current.copy(games = updatedGames)
        }
    }



}
