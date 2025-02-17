package com.example.soccergamesfinder.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.ui.utils.RequestLocationPermission
import com.example.soccergamesfinder.viewmodel.LocationViewModel

@Composable
fun FieldsListScreen(
    navController: NavController,
    viewModel: LocationViewModel = remember { LocationViewModel() }
) {
    val context = LocalContext.current
    val locationState by viewModel.location.collectAsState()
    val hasLocationPermission by viewModel.hasLocationPermission.collectAsState()

    // בדיקת הרשאה כאשר נכנסים למסך
    LaunchedEffect(Unit) {
        viewModel.checkLocationPermission(context)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!hasLocationPermission) {
            Text("ההרשאה למיקום חסרה. אנא אשר את הבקשה.")
            RequestLocationPermission(context as Activity, viewModel)
        } else {
            Button(onClick = { viewModel.requestLocation(context) }) {
                Text("בקש מיקום")
            }

            locationState?.let { location ->
                Text(text = "מיקומך הנוכחי: ${location.latitude}, ${location.longitude}")
            } ?: Text(text = "מיקום עדיין לא זמין")
        }
    }
}

