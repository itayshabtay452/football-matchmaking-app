package com.example.soccergamesfinder.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.ui.screens.profile.ProfileUiEvent
import com.example.soccergamesfinder.ui.screens.profile.UserProfileEvent
import com.example.soccergamesfinder.ui.screens.profile.UserProfileState
import com.example.soccergamesfinder.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _uiEvents = Channel<ProfileUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val user = userRepository.getUserById(userId) ?: return@launch
            _state.update {
                it.copy(
                    fullName = user.fullName,
                    nickname = user.nickname,
                    latitude = user.latitude,
                    longitude = user.longitude,
                    address = null,
                    preferredDays = user.preferredDays,
                    startHour = user.startHour,
                    endHour = user.endHour,
                    birthDate = user.birthDate
                )
            }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.NicknameChanged -> _state.update { it.copy(nickname = event.value) }
            is UserProfileEvent.ProfileImageSelected -> _state.update { it.copy(profileImageUri = event.uri) }
            is UserProfileEvent.LocationUpdated -> _state.update { it.copy(latitude = event.latitude, longitude = event.longitude) }
            is UserProfileEvent.PreferredDaysChanged -> _state.update { it.copy(preferredDays = event.days) }
            is UserProfileEvent.StartHourChanged -> _state.update { it.copy(startHour = event.hour) }
            is UserProfileEvent.EndHourChanged -> _state.update { it.copy(endHour = event.hour) }
            is UserProfileEvent.RequestLocation -> getLocation()
            is UserProfileEvent.SaveProfile -> saveProfile()
            is UserProfileEvent.LocationPermissionDenied -> showError("Location permission denied")
            else -> {} // שדות שלא ניתן לשנות בעריכה (כמו full name) יישארו ריקים
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
                showError("Failed to retrieve location")
            }
        }
    }

    private fun saveProfile() {
        val s = _state.value
        val validations = listOf(
            Validators.validateNicknameFormat(s.nickname),
            Validators.validateLocation(s.latitude, s.longitude),
            Validators.validatePreferredDays(s.preferredDays),
            Validators.validateHourRange(s.startHour, s.endHour)
        )
        val firstError = validations.firstOrNull { !it.isValid }
        if (firstError != null) {
            showError(firstError.errorMessage ?: "Invalid input")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentNicknameTaken = userRepository.isNicknameTaken(s.nickname)
            if (currentNicknameTaken) {
                _state.update { it.copy(isLoading = false) }
                showError("Nickname already in use")
                return@launch
            }


            val result = userRepository.updateCurrentUserProfile(
                nickname = s.nickname,
                latitude = s.latitude!!,
                longitude = s.longitude!!,
                profileImageUri = s.profileImageUri,
                preferredDays = s.preferredDays,
                startHour = s.startHour,
                endHour = s.endHour
            )

            _state.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                _uiEvents.send(ProfileUiEvent.NavigateToHome)
            } else {
                showError("Failed to update profile")
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEvents.send(ProfileUiEvent.ShowError(message))
        }
    }
}

