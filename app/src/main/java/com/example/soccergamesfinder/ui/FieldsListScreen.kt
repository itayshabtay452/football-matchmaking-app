package com.example.soccergamesfinder.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soccergamesfinder.ui.utils.RequestLocationPermission
import com.example.soccergamesfinder.viewmodel.FieldsViewModel
import com.example.soccergamesfinder.viewmodel.LocationViewModel
import com.example.soccergamesfinder.data.SoccerField

@Composable
fun FieldsListScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = remember { LocationViewModel() },
    fieldsViewModel: FieldsViewModel = remember { FieldsViewModel() }
) {
    val context = LocalContext.current
    val locationState by locationViewModel.location.collectAsState()
    val hasLocationPermission by locationViewModel.hasLocationPermission.collectAsState()
    val fieldsWithDistance by fieldsViewModel.fieldsWithDistance.collectAsState()

    // בדיקת הרשאה כאשר נכנסים למסך
    LaunchedEffect(Unit) {
        locationViewModel.checkLocationPermission(context)
        fieldsViewModel.loadFields()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!hasLocationPermission) {
            Text("ההרשאה למיקום חסרה. אנא אשר את הבקשה.")
            RequestLocationPermission(context as Activity, locationViewModel)
        } else {
            Button(onClick = { locationViewModel.requestLocation(context) }) {
                Text("בקש מיקום")
            }

            locationState?.let { location ->
                Text(text = "מיקומך הנוכחי התעדכן.")
                fieldsViewModel.updateUserLocation(location)
            } ?: Text(text = "מיקום עדיין לא זמין")

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(fieldsWithDistance) { (field, distance) ->
                    FieldItem(field, distance, navController)
                }
            }
        }
    }
}

@Composable
fun FieldItem(field: SoccerField, distance: Double?, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("field_details/${field.name.trim()}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = field.name, style = MaterialTheme.typography.headlineSmall)
            distance?.let {
                Text(text = "מרחק: %.2f ק".format(distance), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

