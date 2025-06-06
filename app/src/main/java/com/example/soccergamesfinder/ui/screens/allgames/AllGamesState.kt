package com.example.soccergamesfinder.ui.screens.allgames

import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.model.Game
import com.example.soccergamesfinder.utils.GameFilterManager
import java.time.LocalDate

data class AllGamesState(
    val originalGames: List<Game> = emptyList(),        // כל המשחקים מ-GameListViewModel
    val filteredGames: List<Game> = emptyList(),        // המשחקים לאחר סינון ומיון

    val fieldNameQuery: String = "",                    // חיפוש לפי שם מגרש
    val selectedStatus: GameStatusOption? = null,       // מצב משחק (פתוח, מלא, הסתיים)
    val dateRange: Pair<LocalDate?, LocalDate?> = null to null,   // טווח תאריכים
    val timeRange: Pair<Int?, Int?> = null to null,      // טווח שעות (במונחי שעה ב-24 שעות, לדוג' 14, 18)
    val minPlayers: Int? = null,                        // סינון לפי מינימום שחקנים

    val sortOption: GameFilterManager.SortOption = GameFilterManager.SortOption.BY_TIME,

    val fields: List<Field> = emptyList(), // ⬅️ חדש

    val isLoading: Boolean = false,
    val error: String? = null
)

enum class GameStatusOption {
    OPEN, FULL, ENDED
}
