package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateGameViewModel : ViewModel() {
    private val repository = GameRepository()


    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate

    private val _selectedTimeRange = MutableStateFlow("")
    val selectedTimeRange: StateFlow<String> = _selectedTimeRange

    private val _maxPlayers = MutableStateFlow(10) // ברירת מחדל
    val maxPlayers: StateFlow<Int> = _maxPlayers

    private val _isGameSaved = MutableStateFlow(false)
    val isGameSaved: StateFlow<Boolean> = _isGameSaved

    private val _isSlotTaken = MutableStateFlow(false)
    val isSlotTaken: StateFlow<Boolean> = _isSlotTaken

    private val _field = MutableStateFlow<Field?>(null)
    val field: StateFlow<Field?> = _field

    private val _userHasGameOnDate = MutableStateFlow(false)
    val userHasGameOnDate: StateFlow<Boolean> = _userHasGameOnDate

    fun setField(field: Field) {
        _field.value = field
    }

    fun setDate(date: String) {
        _selectedDate.value = date
    }

    fun setTimeRange(timeRange: String) {
        _selectedTimeRange.value = timeRange
    }

    fun setMaxPlayers(players: Int) {
        _maxPlayers.value = players
    }

    fun saveGame(fieldId: String, userId: String) {
        viewModelScope.launch {
            val userHasGameOnDate = repository.hasUserGameOnDate(userId, selectedDate.value)
            _userHasGameOnDate.value = userHasGameOnDate
            if (userHasGameOnDate) {
                return@launch
            }

            val slotTaken = repository.isGameSlotTaken(fieldId, selectedDate.value, selectedTimeRange.value)
            _isSlotTaken.value = slotTaken

            if (!slotTaken) {
                val game = Game(
                    fieldId = fieldId,
                    date = selectedDate.value,
                    timeRange = selectedTimeRange.value,
                    createdByUserId = userId,
                    maxPlayers = maxPlayers.value,
                    players = listOf(userId) // המשתמש נוסף אוטומטית לרשימת המשתתפים
                )

                val success = repository.createGame(game)
                _isGameSaved.value = success
            }
        }
    }
}

