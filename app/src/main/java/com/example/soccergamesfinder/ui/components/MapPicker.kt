package com.example.soccergamesfinder.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.Composable
import com.google.maps.android.compose.Marker

@Composable
fun MapPicker(
    latitude: Double?,
    longitude: Double?,
    onLocationSelected: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultLat = latitude ?: 31.7683
    val defaultLng = longitude ?: 35.2137

    val markerState = rememberMarkerState(
        position = LatLng(defaultLat, defaultLng)
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 15f)
    }

    // מאזין לשינויים במיקום של הסמן
    LaunchedEffect(markerState.position) {
        onLocationSelected(markerState.position.latitude, markerState.position.longitude)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerState.position = latLng
        }
    ) {
        Marker(
            state = markerState,
            title = "המיקום שבחרת",
            snippet = "ניתן להזיז את הסמן",
            draggable = true
        )
    }
}

