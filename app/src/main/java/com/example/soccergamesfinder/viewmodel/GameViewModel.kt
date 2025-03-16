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

    fun loadGames(fieldId: String) {
        viewModelScope.launch {
            _games.value = repository.getGamesForField(fieldId)
        }
    }

    fun createGame(game: Game) {
        viewModelScope.launch {
            val success = repository.createGame(game)
            if (success) loadGames(game.fieldId)
        }
    }
}