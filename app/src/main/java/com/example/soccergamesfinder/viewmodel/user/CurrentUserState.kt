package com.example.soccergamesfinder.viewmodel.user

import com.example.soccergamesfinder.model.User

data class CurrentUserState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
