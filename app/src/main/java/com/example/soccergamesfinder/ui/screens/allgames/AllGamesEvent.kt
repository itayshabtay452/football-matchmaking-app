package com.example.soccergamesfinder.ui.screens.allgames

import com.example.soccergamesfinder.utils.GameFilterManager
import java.time.LocalDate

sealed class AllGamesEvent {
    data class FieldNameChanged(val value: String) : AllGamesEvent()
    data class StatusChanged(val value: GameStatusOption?) : AllGamesEvent()
    data class DateRangeChanged(val start: LocalDate?, val end: LocalDate?) : AllGamesEvent()
    data class TimeRangeChanged(val startHour: Int?, val endHour: Int?) : AllGamesEvent()
    data class MinPlayersChanged(val value: Int?) : AllGamesEvent()
    data class SortOptionChanged(val option: GameFilterManager.SortOption) : AllGamesEvent()

    object ClearFilters : AllGamesEvent()
    object LoadNextPage : AllGamesEvent() // אם תחליט להוסיף פאגינציה בהמשך
    data class UpdateGames(val newGames: List<com.example.soccergamesfinder.model.Game>) : AllGamesEvent()
}
