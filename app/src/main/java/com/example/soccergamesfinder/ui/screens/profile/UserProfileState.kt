package com.example.soccergamesfinder.ui.screens.profile

import android.net.Uri

/**
 * Holds the UI state for the user profile form.
 * This state is shared between both the CompleteProfileScreen and EditProfileScreen.
 */
data class UserProfileState(
    val fullName: String = "",
    val nickname: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val profileImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val address: String? = null
)