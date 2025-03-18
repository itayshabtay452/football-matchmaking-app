package com.example.soccergamesfinder.ui.screens

import android.Manifest
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
import com.example.soccergamesfinder.viewmodel.LocationViewModel


@Composable
fun CompleteProfileScreen(userViewModel: UserViewModel, navigateToHome: () -> Unit,
                          locationViewModel: LocationViewModel) {

    var name by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    val user by userViewModel.user.collectAsState()
    val userLocation by locationViewModel.userLocation.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationViewModel.fetchUserLocation()
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
        TextField(value = name, onValueChange = { name = it }, label = { Text("שם") })
        TextField(value = nickname, onValueChange = { nickname = it }, label = { Text("כינוי") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("גיל") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(value = city, onValueChange = { city = it }, label = { Text("עיר") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { locationViewModel.requestLocationPermission() }) {
            Text("📍 אפשר גישה למיקום")
        }

            Button(onClick = {
                val ageInt = age.toIntOrNull() ?: 0
                userViewModel.saveUser(name, nickname, ageInt, city,
                    userLocation?.first, userLocation?.second
                )
            }) {
                Text("עדכן פרופיל")
            }
        }
    }


