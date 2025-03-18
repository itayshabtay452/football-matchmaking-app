package com.example.soccergamesfinder.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val context: Context
) : ViewModel() {

    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val userLocation: StateFlow<Pair<Double, Double>?> get() = _userLocation

    private val _locationPermissionRequest = Channel<Boolean>()
    val locationPermissionRequest = _locationPermissionRequest.receiveAsFlow()

    fun requestLocationPermission() {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            fetchUserLocation()
        } else {
            viewModelScope.launch {
                _locationPermissionRequest.send(false)
            }
        }
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            val location = locationRepository.getUserLocation()
            location?.let {
                _userLocation.value = it
            }
        }
    }
}