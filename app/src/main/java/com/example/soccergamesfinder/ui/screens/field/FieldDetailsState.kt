package com.example.soccergamesfinder.ui.screens.field

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game

data class FieldDetailsState(
    val field: Field? = null,
    val games: List<Game> = emptyList(),
    val allGames: List<Game> = emptyList(),
    val totalGames: Int = 0,
    val avgPlayers: Double = 0.0,
    val fullGames: Int = 0,
    val openGames: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
