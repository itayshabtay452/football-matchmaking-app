package com.example.soccergamesfinder.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiEvent {
    object NavigateToHome : ProfileUiEvent()
    data class ShowError(val message: String) : ProfileUiEvent()
}


/**
 * ViewModel responsible for managing the user profile completion flow.
 * Handles validation, location, and interaction with repositories.
 */
@HiltViewModel
class CompleteProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _uiEvents = Channel<ProfileUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.FullNameChanged -> {
                _state.update { it.copy(fullName = event.value) }
            }

            is UserProfileEvent.NicknameChanged -> {
                _state.update { it.copy(nickname = event.value) }
            }

            is UserProfileEvent.ProfileImageSelected -> {
                _state.update { it.copy(profileImageUri = event.uri) }
            }

            is UserProfileEvent.LocationUpdated -> {
                _state.update { it.copy(latitude = event.latitude, longitude = event.longitude) }
            }

            is UserProfileEvent.LocationPermissionDenied -> {
                showError("Location permission denied")
            }

            is UserProfileEvent.RequestLocation -> {
                getLocation()
            }

            is UserProfileEvent.SaveProfile -> {
                saveProfile()
            }
        }
    }

    private fun getLocation() {
        viewModelScope.launch {
            val result = locationService.getCurrentLocation()
            result.onSuccess { (lat, lon) ->
                val address = locationService.getAddressFromLocation(lat, lon)
                _state.update { it.copy(address = address) }
                onEvent(UserProfileEvent.LocationUpdated(lat, lon))
            }.onFailure {
                showError("Failed to retrieve location")            }
        }
    }

    private fun saveProfile() {
        val state = _state.value

        val fullNameResult = Validators.validateFullName(state.fullName)
        val nicknameFormatResult = Validators.validateNicknameFormat(state.nickname)
        val locationResult = Validators.validateLocation(state.latitude, state.longitude)

        val validations = listOf(fullNameResult, nicknameFormatResult, locationResult)
        val firstError = validations.firstOrNull { !it.isValid }

        if (firstError != null) {
            showError(firstError.errorMessage ?: "Invalid input")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // בודק אם הכינוי תפוס
            val nicknameTaken = userRepository.isNicknameTaken(state.nickname)
            if (nicknameTaken) {
                _state.update { it.copy(isLoading = false) }
                showError("Nickname already in use")
                return@launch
            }

            val result = userRepository.createUser(
                fullName = state.fullName,
                nickname = state.nickname,
                latitude = state.latitude!!,
                longitude = state.longitude!!,
                profileImageUri = state.profileImageUri
            )

            _state.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                _uiEvents.send(ProfileUiEvent.NavigateToHome)
            } else {
                showError("Failed to save profile")
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEvents.send(ProfileUiEvent.ShowError(message))
        }
    }
}