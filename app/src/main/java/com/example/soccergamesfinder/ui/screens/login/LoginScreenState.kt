// 📁 presentation/login/LoginScreenState.kt
package com.example.soccergamesfinder.ui.screens.login

/**
 * Holds the input and loading state for the login screen.
 */

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)