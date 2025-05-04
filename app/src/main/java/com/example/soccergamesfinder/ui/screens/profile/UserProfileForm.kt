package com.example.soccergamesfinder.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.soccergamesfinder.ui.components.shared.BirthDatePickerField
import com.example.soccergamesfinder.ui.components.shared.HourPickerDropdownModern

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
    val scrollState = rememberScrollState()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {         println("DEBUG URI userprofileform: $it") // אפשר גם להוסיף Log.d
            onEvent(UserProfileEvent.ProfileImageSelected(it)) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
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

        state.birthDate?.let {
            BirthDatePickerField(
                selectedDate = it,
                onDateSelected = { onEvent(UserProfileEvent.BirthDateChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text("ימים מועדפים")
        PreferredDaysRow(
            selectedDays = state.preferredDays,
            onSelectionChanged = { onEvent(UserProfileEvent.PreferredDaysChanged(it)) }
        )

        Text("טווח שעות מועדף")

        HourPickerDropdownModern(
            selectedHour = state.startHour,
            onHourSelected = { it?.let { hour -> onEvent(UserProfileEvent.StartHourChanged(hour)) } },
            label = "שעת התחלה"
        )

        HourPickerDropdownModern(
            selectedHour = state.endHour,
            onHourSelected = { it?.let { hour -> onEvent(UserProfileEvent.EndHourChanged(hour)) } },
            label = "שעת סיום"
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

@Composable
fun PreferredDaysRow(
    selectedDays: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val days = listOf("ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { day ->
            val isSelected = selectedDays.contains(day)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val updated = if (isSelected) selectedDays - day else selectedDays + day
                    onSelectionChanged(updated)
                },
                label = { Text(day) }
            )
        }
    }
}

