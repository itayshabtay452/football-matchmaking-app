package com.example.soccergamesfinder.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.soccergamesfinder.utils.ValidationResult
import com.example.soccergamesfinder.viewmodel.LocationViewModel
import com.example.soccergamesfinder.viewmodel.UserViewModel

@Composable
fun CompleteProfileScreen(
    userViewModel: UserViewModel,
    navigateToHome: () -> Unit,
    locationViewModel: LocationViewModel
) {
    var name by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageSize by remember { mutableStateOf<Long?>(null) }

    val user by userViewModel.user.collectAsState()
    val userLocation by locationViewModel.userLocation.collectAsState()

    val nameValidation by userViewModel.nameValidation.collectAsState()
    val nicknameValidation by userViewModel.nicknameValidation.collectAsState()
    val ageValidation by userViewModel.ageValidation.collectAsState()
    val locationValidation by userViewModel.locationValidation.collectAsState()
    val imageValidation by userViewModel.imageValidation.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationViewModel.fetchUserLocation()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        if (uri != null) {
            imageSize = userViewModel.getImageSize(uri)
        }
    }

    LaunchedEffect(Unit) {
        locationViewModel.locationPermissionRequest.collect { shouldRequest ->
            if (!shouldRequest) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            navigateToHome()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        NameField(name, nameValidation) { name = it }
        NicknameField(nickname, nicknameValidation) { nickname = it }
        AgeField(age, ageValidation) { age = it }

        LocationPermissionButton(
            onClick = { locationViewModel.requestLocationPermission() },
            validation = locationValidation
        )

        ImagePickerSection(
            imageUri = imageUri,
            imageValidation = imageValidation,
            onPickImage = { imagePickerLauncher.launch("image/*") }
        )

        SubmitButton {
            userViewModel.validateAndSaveUser(
                name, nickname, age.toIntOrNull(),
                userLocation?.first, userLocation?.second, imageUri, imageSize
            )
        }
    }
}

@Composable
fun NameField(name: String, validation: ValidationResult, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("שם") },
        isError = validation is ValidationResult.Error,
        supportingText = { (validation as? ValidationResult.Error)?.message?.let { Text(it) } }
    )
}

@Composable
fun NicknameField(nickname: String, validation: ValidationResult, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = nickname,
        onValueChange = onValueChange,
        label = { Text("כינוי") },
        isError = validation is ValidationResult.Error,
        supportingText = { (validation as? ValidationResult.Error)?.message?.let { Text(it) } }
    )
}

@Composable
fun AgeField(age: String, validation: ValidationResult, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = age,
        onValueChange = onValueChange,
        label = { Text("גיל") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = validation is ValidationResult.Error,
        supportingText = { (validation as? ValidationResult.Error)?.message?.let { Text(it) } }
    )
}

@Composable
fun LocationPermissionButton(onClick: () -> Unit, validation: ValidationResult) {
    Button(onClick = onClick) {
        Text("📍 אפשר גישה למיקום")
    }
    if (validation is ValidationResult.Error) {
        Text(validation.message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun ImagePickerSection(
    imageUri: Uri?,
    imageValidation: ValidationResult,
    onPickImage: () -> Unit
) {
    Button(onClick = onPickImage) {
        Text("📷 בחר תמונת פרופיל")
    }
    imageUri?.let {
        Text("✅ תמונה נבחרה!", style = MaterialTheme.typography.bodyMedium)
    }
    if (imageValidation is ValidationResult.Error) {
        Text(imageValidation.message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("עדכן פרופיל")
    }
}
