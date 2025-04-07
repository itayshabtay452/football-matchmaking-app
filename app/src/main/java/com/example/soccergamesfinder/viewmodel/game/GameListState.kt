package com.example.soccergamesfinder.viewmodel.game

import com.example.soccergamesfinder.data.Game

data class GameListState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
