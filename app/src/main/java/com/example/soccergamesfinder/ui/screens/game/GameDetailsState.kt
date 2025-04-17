package com.example.soccergamesfinder.ui.screens.game

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User

data class GameDetailsState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val game: Game? = null,
    val field: Field? = null,
    val creator: User? = null,
    val participants: List<User> = emptyList()
)
