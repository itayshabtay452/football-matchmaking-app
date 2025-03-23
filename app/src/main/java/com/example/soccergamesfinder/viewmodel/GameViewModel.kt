package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.repository.GameRepository
import com.example.soccergamesfinder.utils.GameValidator
import com.example.soccergamesfinder.utils.ValidationResult
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage.asStateFlow()

    fun loadGames(fieldId: String) {
        viewModelScope.launch {
            _games.value = repository.getGamesForField(fieldId)
        }
    }

    private fun createGame(game: Game) {
        viewModelScope.launch {
            val gameWithCreator = game.copy(players = listOf(game.creatorId))
            val success = repository.createGame(gameWithCreator)
            if (success) loadGames(game.fieldId)
        }
    }

    fun validateAndCreateGame(newGame: Game, userId: String,
                              onResult: (ValidationResult) -> Unit) {
        _errorMessage.value = null
        viewModelScope.launch {
            val userGames = repository.getUserGames(userId)
            val validationResult = GameValidator.validateGameSlot(newGame, _games.value, userGames)

            if (validationResult is ValidationResult.Success) {
                createGame(newGame)
                loadGames(newGame.fieldId)
                onResult(ValidationResult.Success)
            } else {
                _errorMessage.value = (validationResult as ValidationResult.Error).message
                onResult(validationResult)
            }
        }
    }

    fun validateAndJoinGame(game: Game, userId: String, onResult: (ValidationResult) -> Unit) {
        _errorMessage.value = null
        viewModelScope.launch {
            val userGames = repository.getUserGames(userId)
            val validationResult = GameValidator.validateJoinGame(game, userId, userGames)

            if (validationResult is ValidationResult.Success) {
                val updatedPlayers = game.players + userId
                repository.updatePlayersList(game.id, updatedPlayers)
                _game.value = _game.value?.copy(players = updatedPlayers)
                onResult(ValidationResult.Success)
            } else {
                _errorMessage.value = (validationResult as ValidationResult.Error).message
                onResult(validationResult)
             }
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