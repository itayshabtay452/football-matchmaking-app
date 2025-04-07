package com.example.soccergamesfinder.ui.screens.home

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game

data class HomeState(
    val userNickname: String = "",
    val userProfileImageUrl: String? = null,
    val fields: List<Field> = emptyList(),
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)