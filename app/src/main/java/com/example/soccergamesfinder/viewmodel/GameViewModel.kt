package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> get() = _games.asStateFlow()

    private val _game = MutableStateFlow<Game?>(null)
    val game: StateFlow<Game?> get() = _game.asStateFlow()

    fun loadGames(fieldId: String) {
        viewModelScope.launch {
            _games.value = repository.getGamesForField(fieldId)
        }
    }

    fun createGame(game: Game) {
        viewModelScope.launch {
            val gameWithCreator = game.copy(players = listOf(game.creatorId))
            val success = repository.createGame(gameWithCreator)
            if (success) loadGames(game.fieldId)
        }
    }

    fun joinGame(game: Game, userId: String) {
        if (userId !in game.players && !game.isGameFull()) {
            viewModelScope.launch {
                val updatedPlayers = game.players + userId

                val success = repository.updatePlayersList(game.id, updatedPlayers)
                if (success) {
                    _game.value = _game.value?.copy(players = updatedPlayers)
                }
            }
        } else {
            println("🚫 המשחק מלא או שהמשתמש כבר רשום")
        }
    }


    fun deleteGame(game: Game, userId: String) {
        if (game.creatorId == userId) {
            viewModelScope.launch {
                repository.deleteGame(game.id)
            }
        }
    }

    fun leaveGame(game: Game, userId: String) {
        if (userId in game.players && userId != game.creatorId) {
            viewModelScope.launch {
                val updatedPlayers = game.players - userId
                repository.updatePlayersList(game.id, updatedPlayers)
                _game.value = _game.value?.copy(players = updatedPlayers)
            }
        }
    }

    fun getGame(gameId: String) {
        viewModelScope.launch {
            _game.value = repository.getGameById(gameId)
        }
    }
}