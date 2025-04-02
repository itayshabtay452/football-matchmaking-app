package com.example.soccergamesfinder.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * A reusable composable form for collecting or editing user profile data.
 * Can be used in both profile creation and editing flows.
 *
 * @param state The current state of the profile form.
 * @param onEvent A lambda for dispatching user actions (e.g. field change, location request).
 * @param onSubmit Called when the user presses the save/finish button.
 * @param isEditMode If true, disables editing of the full name field.
 */
@Composable
fun UserProfileForm(
    state: UserProfileState,
    onEvent: (UserProfileEvent) -> Unit,
    onSubmit: () -> Unit,
    isEditMode: Boolean,
    onRequestLocationPermission: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {         println("DEBUG URI userprofileform: $it") // אפשר גם להוסיף Log.d
            onEvent(UserProfileEvent.ProfileImageSelected(it)) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isEditMode) "Edit Profile" else "Complete Your Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = state.fullName,
            onValueChange = { onEvent(UserProfileEvent.FullNameChanged(it)) },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isEditMode // full name is not editable in edit mode
        )

        OutlinedTextField(
            value = state.nickname,
            onValueChange = { onEvent(UserProfileEvent.NicknameChanged(it)) },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onRequestLocationPermission() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share My Location")
        }

        state.address?.let {
            Text("📍 $it")
        }

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose Profile Picture")
        }

        state.profileImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Save Changes" else "Finish")
            }
        }
    }
}
