// LocationViewModel.kt
package com.example.soccergamesfinder.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    init {
        requestLocation() // טוען מיקום מיד כשה-ViewModel נוצר
    }

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        val context = getApplication<Application>().applicationContext
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }


        viewModelScope.launch {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    _currentLocation.value = location
                }
                .addOnFailureListener {
                    _currentLocation.value = null // אם נכשל, שמור `null`
                }
        }
    }
}
