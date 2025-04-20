package com.example.soccergamesfinder.ui.screens.home

data class HomeScreenNavActions(
    val navigateToProfile: (String) -> Unit,
    val navigateToAllFields: () -> Unit,
    val navigateToAllGames: () -> Unit,
    val navigateToField: (String) -> Unit,
    val navigateToGame: (String) -> Unit,
    val navigateToLogin: () -> Unit,
    val navigateToAddField: () -> Unit
)
