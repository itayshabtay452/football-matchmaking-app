package com.example.soccergamesfinder.ui.screens.login

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.AuthRepository
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.services.GoogleSignInService
import com.example.soccergamesfinder.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing login logic and state.
 * Handles validation, email/password login, Google login, and navigation logic.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val googleSignInService: GoogleSignInService
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state: StateFlow<LoginScreenState> = _state

    private val _uiEvents = Channel<LoginUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    /**
     * Updates the email in state.
     */

    fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    /**
     * Validates input and performs email/password login.
     * Calls [onNavigate] with a boolean indicating if user has profile.
     */
    fun login() {
        val (email, password) = validateInputs() ?: return

        viewModelScope.launch {
            setLoading(true)
            val result = authRepository.loginWithEmailPassword(email, password)
            setLoading(false)

            if (result.isSuccess) {
                navigateBasedOnProfile()
            } else {
                sendEvent(LoginUiEvent.ShowError(result.exceptionOrNull()?.message ?: "Login failed"))
            }
        }
    }

    fun register() {
        val (email, password) = validateInputs() ?: return

        viewModelScope.launch {
            setLoading(true)
            val result = authRepository.registerWithEmailPassword(email, password)
            setLoading(false)

            if (result.isSuccess) {
                sendEvent(LoginUiEvent.NavigateToCompleteProfile)
            } else {
                sendEvent(LoginUiEvent.ShowError(result.exceptionOrNull()?.message ?: "Registration failed"))
            }
        }
    }

    private fun validateInputs(): Pair<String, String>? {
        val email = _state.value.email.trim()
        val password = _state.value.password.trim()

        val emailValidation = Validators.validateEmail(email)
        val passwordValidation = Validators.validatePassword(password)

        val firstError = listOf(emailValidation, passwordValidation).firstOrNull { !it.isValid }
        return if (firstError != null) {
            sendEvent(LoginUiEvent.ShowError(firstError.errorMessage ?: "Invalid input"))
            null
        } else email to password
    }

    /**
     * Returns the intent to launch the Google Sign-In flow.
     */
    fun getGoogleSignInIntent(): Intent = googleSignInService.getSignInIntent()

    fun onGoogleSignInClicked() {
        sendEvent(LoginUiEvent.StartGoogleSignIn)
    }

    /**
     * Handles the result of the Google Sign-In activity.
     * Calls [onNavigate] with a boolean indicating if user has profile.
     */
    fun onGoogleSignInResult(result: ActivityResult) {
        val tokenResult = googleSignInService.extractIdToken(result)
        if (tokenResult == null) {
            sendEvent(LoginUiEvent.ShowError("Failed to retrieve ID token"))
            return
        }

        viewModelScope.launch {
            setLoading(true)
            val loginResult = authRepository.loginWithGoogle(tokenResult)
            setLoading(false)

            if (loginResult.isSuccess) {
                navigateBasedOnProfile()
            } else {
                sendEvent(LoginUiEvent.ShowError(loginResult.exceptionOrNull()?.message ?: "Google sign-in failed"))
            }
        }
    }

    /**
     * Helper to update the UI state.
     */
    private fun navigateBasedOnProfile() {
        viewModelScope.launch {
            val hasProfile = userRepository.hasUserData()
            sendEvent(if (hasProfile) LoginUiEvent.NavigateToHome else LoginUiEvent.NavigateToCompleteProfile)
        }
    }

    private fun setLoading(loading: Boolean) {
        _state.value = _state.value.copy(isLoading = loading)
    }

    private fun sendEvent(event: LoginUiEvent) {
        viewModelScope.launch { _uiEvents.send(event) }
    }
}