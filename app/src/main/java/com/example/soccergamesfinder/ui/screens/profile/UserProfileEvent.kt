package com.example.soccergamesfinder.ui.screens.profile

import android.net.Uri

/**
 * Represents user interactions within the user profile form.
 * These events are handled by the ViewModel to update the state or trigger actions.
 */
sealed class UserProfileEvent {
    data class FullNameChanged(val value: String) : UserProfileEvent()
    data class NicknameChanged(val value: String) : UserProfileEvent()
    data class ProfileImageSelected(val uri: Uri) : UserProfileEvent()
    data class LocationUpdated(val latitude: Double, val longitude: Double) : UserProfileEvent()
    object LocationPermissionDenied : UserProfileEvent()
    object RequestLocation : UserProfileEvent()
    object SaveProfile : UserProfileEvent()
}