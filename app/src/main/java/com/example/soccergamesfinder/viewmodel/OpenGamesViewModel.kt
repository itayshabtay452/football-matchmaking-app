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
    val distanceFromUser: Double? = null, // נוסיף מרחק מהמשתמש
    val createdByUserName: String // נוסיף את שם המשתמש שפתח את המשחק
)

class OpenGamesViewModel : ViewModel() {
    private val gameRepository = GameRepository()
    private val fieldsRepository = FieldsRepository()
    private val userRepository = UserRepository() // נוסיף את UserRepository

    private val _userLocation = MutableStateFlow<Location?>(null) // נשמור את המיקום
    val userLocation: StateFlow<Location?> = _userLocation

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _openGames = MutableStateFlow<List<GameWithFieldName>>(emptyList())
    val openGames: StateFlow<List<GameWithFieldName>> = _openGames

    private val _maxDistanceKm = MutableStateFlow(10) // ברירת מחדל 10 ק"מ
    val maxDistanceKm: StateFlow<Int> = _maxDistanceKm

    private val _userHasGameOnDate = MutableStateFlow(false)
    val userHasGameOnDate: StateFlow<Boolean> = _userHasGameOnDate

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
                val createdByUserName = userRepository.getUserDisplayName(game.createdByUserId) // שליפת שם המשתמש

                val distance = userLocation?.let { location ->
                    fieldLocation?.let {
                        val loc = Location("").apply {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                        location.distanceTo(loc) / 1000.0 // המרחק בק"מ
                    }
                }

                GameWithFieldName(game, field,distance, createdByUserName)
            }
                .filter { it.distanceFromUser == null || it.distanceFromUser <= _maxDistanceKm.value } // מסנן לפי מרחק
                .sortedBy { it.game.date } // ממיין לפי תאריך

            _openGames.value = gamesWithFieldNames
        }
    }

    fun joinGame(gameId: String, userId: String, gameDate: String) {
        viewModelScope.launch {
            // בדיקה אם המשתמש כבר רשום למשחק באותו יום
            val userHasGame = gameRepository.hasUserGameOnDate(userId, gameDate)
            if (userHasGame) {
                _errorMessage.value = "אתה כבר רשום למשחק אחר בתאריך זה!" // עדכון הודעת השגיאה
                _userHasGameOnDate.value = true // נעדכן שהמשתמש כבר רשום למשחק אחר באותו יום
                return@launch
            }

            val success = gameRepository.joinGame(gameId, userId)
            if (success) {
                _errorMessage.value = null
                loadOpenGames(_userLocation.value) // טוען מחדש כדי להציג את המשתמש החדש
            }
        }
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }



}
