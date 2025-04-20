
// AddFieldScreen.kt
package com.example.soccergamesfinder.ui.screens.addfield

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soccergamesfinder.ui.components.MapPicker

@Composable
fun AddFieldScreen(
    viewModel: AddFieldViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // מאזין לאירועים חיצוניים
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.NavigateBack -> onNavigateBack()
                is UiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(AddFieldEvent.NameChanged(it)) },
                label = { Text("שם המגרש") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AddFieldEvent.DescriptionChanged(it)) },
                label = { Text("תיאור (אופציונלי)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = state.size,
                onValueChange = { viewModel.onEvent(AddFieldEvent.SizeChanged(it)) },
                label = { Text("גודל המגרש") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.lighting,
                    onCheckedChange = { viewModel.onEvent(AddFieldEvent.LightingChanged(it)) }
                )
                Text("יש תאורה")
            }
        }

        item {
            OutlinedTextField(
                value = state.address,
                onValueChange = { viewModel.onEvent(AddFieldEvent.AddressChanged(it)) },
                label = { Text("כתובת") },
                modifier = Modifier.fillMaxWidth()
            )
            if (state.isMapVisible) {
                Text("בחר מיקום מדויק על המפה:")
                MapPicker(
                    latitude = state.mapLatitude,
                    longitude = state.mapLongitude,
                    onLocationSelected = { lat, lng ->
                        viewModel.onEvent(AddFieldEvent.MapMarkerMoved(lat, lng))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }

        item {
            Button(
                onClick = { viewModel.onEvent(AddFieldEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("📤 שלח מגרש")
                }
            }
        }
    }
}

