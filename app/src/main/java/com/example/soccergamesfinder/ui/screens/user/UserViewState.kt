// 📁 ui/screens/user/UserViewState.kt
package com.example.soccergamesfinder.ui.screens.user

import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.User

data class UserViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val games: List<Game> = emptyList(),
    val isOwnProfile: Boolean = false
)
