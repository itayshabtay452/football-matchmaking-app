package com.example.soccergamesfinder.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.ui.screens.allgames.GameStatusOption
import java.time.LocalDate
import java.time.ZoneId

object GameFilterManager {

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterGames(
        games: List<Game>,
        fieldNameQuery: String = "",
        status: GameStatusOption? = null,
        dateRange: Pair<LocalDate?, LocalDate?> = null to null,
        timeRange: Pair<Int?, Int?> = null to null,
        minPlayers: Int? = null,
        fieldNameResolver: (String) -> String? = { null }
    ): List<Game> {
        return games.filter { game ->
            val fieldName = fieldNameResolver(game.fieldId)

            val matchesFieldName = fieldNameQuery.isBlank() ||
                    fieldName?.contains(fieldNameQuery, ignoreCase = true) == true

            val matchesStatus = when (status) {
                GameStatusOption.OPEN -> game.status.name == "OPEN"
                GameStatusOption.FULL -> game.status.name == "FULL"
                GameStatusOption.ENDED -> game.status.name == "ENDED"
                null -> true
            }

            val gameDateTime = game.startTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val gameDate = gameDateTime.toLocalDate()
            val gameHour = gameDateTime.hour

            val matchesDate = dateRange.first?.let { gameDate >= it } != false &&
                    dateRange.second?.let { gameDate <= it } != false

            val matchesTime = timeRange.first?.let { gameHour >= it } != false &&
                    timeRange.second?.let { gameHour <= it } != false

            val matchesPlayers = minPlayers == null || game.joinedPlayers.size >= minPlayers

            matchesFieldName && matchesStatus && matchesDate && matchesTime && matchesPlayers
        }
    }

    fun sortGames(
        games: List<Game>,
        sortBy: SortOption,
        distanceResolver: (String) -> Double? = { null }
    ): List<Game> {
        return when (sortBy) {
            SortOption.BY_DISTANCE -> games.sortedBy {
                distanceResolver(it.fieldId) ?: Double.MAX_VALUE
            }
            SortOption.BY_PLAYER_COUNT -> games.sortedByDescending { it.joinedPlayers.size }
            SortOption.BY_TIME -> games.sortedBy { it.startTime }
        }
    }

    enum class SortOption {
        BY_DISTANCE,
        BY_PLAYER_COUNT,
        BY_TIME
    }
}
