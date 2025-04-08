// 📁 ui/screens/profile/CompleteProfileScreen.kt
package com.example.soccergamesfinder.ui.screens.profile

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.LocationPermissionHandler

/**
 * Screen for completing the user's profile after login or sign-up.
 * Collects full name, nickname, profile picture, and location.
 */
@Composable
fun CompleteProfileScreen(
    viewModel: CompleteProfileViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val shouldRequestPermission = remember { mutableStateOf(false) }


    // Handle one-time UI events (e.g., navigation, errors)
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is ProfileUiEvent.NavigateToHome -> onNavigateToHome()
                is ProfileUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Request location permission and trigger location loading
    if (shouldRequestPermission.value) {
        LocationPermissionHandler(
            onPermissionGranted = {
                viewModel.onEvent(UserProfileEvent.RequestLocation)
                shouldRequestPermission.value = false
            },
            onPermissionDenied = {
                viewModel.onEvent(UserProfileEvent.LocationPermissionDenied)
                shouldRequestPermission.value = false
            }
        )
    }

    // Render the reusable form
    UserProfileForm(
        state = state,
        onEvent = viewModel::onEvent,
        onSubmit = { viewModel.onEvent(UserProfileEvent.SaveProfile) },
        isEditMode = false,
        onRequestLocationPermission = { shouldRequestPermission.value = true }
    )
}
