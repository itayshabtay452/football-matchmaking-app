package com.example.soccergamesfinder.ui.screens.editprofile

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.LocationPermissionHandler
import com.example.soccergamesfinder.ui.screens.profile.ProfileUiEvent
import com.example.soccergamesfinder.ui.screens.profile.UserProfileEvent
import com.example.soccergamesfinder.ui.screens.profile.UserProfileForm

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val shouldRequestPermission = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is ProfileUiEvent.NavigateToHome -> onNavigateBack()
                is ProfileUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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

    UserProfileForm(
        state = state,
        onEvent = viewModel::onEvent,
        onSubmit = { viewModel.onEvent(UserProfileEvent.SaveProfile) },
        isEditMode = true,
        onRequestLocationPermission = { shouldRequestPermission.value = true }
    )
}
