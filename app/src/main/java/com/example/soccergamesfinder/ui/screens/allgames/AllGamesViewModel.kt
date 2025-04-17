package com.example.soccergamesfinder.ui.screens.allgames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.utils.GameFilterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllGamesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AllGamesState())
    val state: StateFlow<AllGamesState> = _state.asStateFlow()


    private var lastGames: List<Game> = emptyList()
    private var lastFields: List<Field> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDataChanged(games: List<Game>, fields: List<Field>) {
        if (games != lastGames || fields != lastFields) {
            lastGames = games
            lastFields = fields
            _state.update {
                it.copy(
                    originalGames = games,
                    fields = fields
                )
            }
            applyFiltersAndSorting()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: AllGamesEvent) {
        when (event) {
            is AllGamesEvent.FieldNameChanged -> _state.update { it.copy(fieldNameQuery = event.value) }
            is AllGamesEvent.StatusChanged -> _state.update { it.copy(selectedStatus = event.value) }
            is AllGamesEvent.DateRangeChanged -> _state.update { it.copy(dateRange = event.start to event.end) }
            is AllGamesEvent.TimeRangeChanged -> _state.update { it.copy(timeRange = event.startHour to event.endHour) }
            is AllGamesEvent.MinPlayersChanged -> _state.update { it.copy(minPlayers = event.value) }
            is AllGamesEvent.SortOptionChanged -> _state.update { it.copy(sortOption = event.option) }
            is AllGamesEvent.ClearFilters -> _state.update {
                it.copy(
                    fieldNameQuery = "",
                    selectedStatus = null,
                    dateRange = null to null,
                    timeRange = null to null,
                    minPlayers = null,
                    sortOption = GameFilterManager.SortOption.BY_TIME
                )
            }

            AllGamesEvent.LoadNextPage -> TODO()
            is AllGamesEvent.UpdateGames -> TODO()
        }

        applyFiltersAndSorting()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyFiltersAndSorting() {
        val s = _state.value
        val fields = s.fields

        val filtered = GameFilterManager.filterGames(
            games = s.originalGames,
            fieldNameQuery = s.fieldNameQuery,
            status = s.selectedStatus,
            dateRange = s.dateRange,
            timeRange = s.timeRange,
            minPlayers = s.minPlayers,
            fieldNameResolver = { id -> fields.firstOrNull { it.id == id }?.name }
        )

        val sorted = GameFilterManager.sortGames(
            games = filtered,
            sortBy = s.sortOption,
            distanceResolver = { id -> fields.firstOrNull { it.id == id }?.distance }
        )

        _state.update { it.copy(filteredGames = sorted) }
    }

}
