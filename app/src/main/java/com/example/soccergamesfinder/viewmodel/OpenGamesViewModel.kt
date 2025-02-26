package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GameWithFieldName(
    val game: Game,
    val fieldName: String
)

class OpenGamesViewModel : ViewModel() {
    private val gameRepository = GameRepository()
    private val fieldsRepository = FieldsRepository()

    private val _openGames = MutableStateFlow<List<GameWithFieldName>>(emptyList())
    val openGames: StateFlow<List<GameWithFieldName>> = _openGames

    fun loadOpenGames() {
        viewModelScope.launch {
            val games = gameRepository.getAllGames()
            val gamesWithFieldNames = games.map { game ->
                val fieldName = fieldsRepository.getFieldNameById(game.fieldId)
                GameWithFieldName(game, fieldName)
            }
            _openGames.value = gamesWithFieldNames
        }
    }
}
