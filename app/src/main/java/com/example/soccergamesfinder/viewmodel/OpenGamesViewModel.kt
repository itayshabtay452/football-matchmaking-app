package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.location.Location

data class GameWithFieldName(
    val game: Game,
    val fieldName: String,
    val distanceFromUser: Double? = null // נוסיף מרחק מהמשתמש
)

class OpenGamesViewModel : ViewModel() {
    private val gameRepository = GameRepository()
    private val fieldsRepository = FieldsRepository()

    private val _userLocation = MutableStateFlow<Location?>(null) // נשמור את המיקום
    val userLocation: StateFlow<Location?> = _userLocation

    private val _openGames = MutableStateFlow<List<GameWithFieldName>>(emptyList())
    val openGames: StateFlow<List<GameWithFieldName>> = _openGames

    private val _maxDistanceKm = MutableStateFlow(10) // ברירת מחדל 10 ק"מ
    val maxDistanceKm: StateFlow<Int> = _maxDistanceKm

    fun setMaxDistance(distance: Int) {
        _maxDistanceKm.value = distance
        _userLocation.value?.let { loadOpenGames(it) } // אם יש מיקום - נעדכן את הנתונים
    }

    fun loadOpenGames(userLocation: Location?) {
        _userLocation.value = userLocation // שומרים את המיקום
        viewModelScope.launch {
            val games = gameRepository.getAllGames()
            val gamesWithFieldNames = games.map { game ->
                val field = fieldsRepository.getFieldNameById(game.fieldId)
                val fieldLocation = fieldsRepository.getFieldLocationById(game.fieldId)

                val distance = userLocation?.let { location ->
                    fieldLocation?.let {
                        val loc = Location("").apply {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                        location.distanceTo(loc) / 1000.0 // המרחק בק"מ
                    }
                }

                GameWithFieldName(game, field, distance)
            }
                .filter { it.distanceFromUser == null || it.distanceFromUser <= _maxDistanceKm.value } // מסנן לפי מרחק
                .sortedBy { it.game.date } // ממיין לפי תאריך

            _openGames.value = gamesWithFieldNames
        }
    }

    fun joinGame(gameId: String, userId: String) {
        viewModelScope.launch {
            val success = gameRepository.joinGame(gameId, userId)
            if (success) {
                loadOpenGames(_userLocation.value) // טוען מחדש כדי להציג את המשתמש החדש
            }
        }
    }

}
