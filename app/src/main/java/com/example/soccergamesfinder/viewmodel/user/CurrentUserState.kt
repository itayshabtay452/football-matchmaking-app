package com.example.soccergamesfinder.viewmodel.user

import com.example.soccergamesfinder.data.User

data class CurrentUserState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
