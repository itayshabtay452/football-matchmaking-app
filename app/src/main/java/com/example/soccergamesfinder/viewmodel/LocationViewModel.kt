package com.example.soccergamesfinder.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: StateFlow<Boolean> = _hasLocationPermission

    fun checkLocationPermission(context: Context) {
        _hasLocationPermission.value = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun updatePermissionStatus(isGranted: Boolean) {
        _hasLocationPermission.value = isGranted
    }

    fun requestLocation(context: Context) {
        if (_hasLocationPermission.value) {
            viewModelScope.launch {
                val locationService = LocationService(context)
                val userLocation = locationService.getCurrentLocation()
                _location.value = userLocation
            }
        }
    }
}
