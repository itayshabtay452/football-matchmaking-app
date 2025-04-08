package com.example.soccergamesfinder.ui.screens.creategame

data class CreateGameFormState(
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val maxPlayers: Int = 0,
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
