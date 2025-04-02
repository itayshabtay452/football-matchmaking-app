package com.example.soccergamesfinder.ui.screens.login

/**
 * Events emitted from LoginViewModel to notify the UI.
 */

sealed class LoginUiEvent {
    object StartGoogleSignIn : LoginUiEvent()
    object NavigateToHome : LoginUiEvent()
    object NavigateToCompleteProfile : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}