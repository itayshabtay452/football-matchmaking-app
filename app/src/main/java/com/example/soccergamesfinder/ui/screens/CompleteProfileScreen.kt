package com.example.soccergamesfinder.ui.screens

import android.Manifest
import android.net.Uri
import android.util.Log
import com.example.soccergamesfinder.viewmodel.UserViewModel
import com.example.soccergamesfinder.data.User
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


@Composable
fun CompleteProfileScreen(userViewModel: UserViewModel, navigateToHome: () -> Unit,
                          locationViewModel: LocationViewModel) {

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
    ) { uri -> imageUri = uri
        if (uri != null){
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
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("שם") },
            isError = nameValidation is ValidationResult.Error,
            supportingText = { (nameValidation as? ValidationResult.Error)?.message?.let { Text(it) } }
        )

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("כינוי") },
            isError = nicknameValidation is ValidationResult.Error,
            supportingText = { (nicknameValidation as? ValidationResult.Error)?.message?.let { Text(it) } }
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("גיל") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = ageValidation is ValidationResult.Error,
            supportingText = { (ageValidation as? ValidationResult.Error)?.message?.let { Text(it) } }
        )

        Button(onClick = { locationViewModel.requestLocationPermission() }) {
            Text("📍 אפשר גישה למיקום")
        }

        if (locationValidation is ValidationResult.Error) {
            Text((locationValidation as ValidationResult.Error).message, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("📷 בחר תמונת פרופיל")
        }

        imageUri?.let {
            Text("✅ תמונה נבחרה!", style = MaterialTheme.typography.bodyMedium)
        }

        if (imageValidation is ValidationResult.Error) {
            Text((imageValidation as ValidationResult.Error).message, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            userViewModel.validateAndSaveUser(
                name, nickname, age.toIntOrNull(),
                userLocation?.first, userLocation?.second, imageUri, imageSize
            )
        }) {
            Text("עדכן פרופיל")
        }

    }
}