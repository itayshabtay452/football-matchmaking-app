package com.example.soccergamesfinder.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.FirestoreRepository
import com.example.soccergamesfinder.data.SoccerField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FieldsViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _fields = MutableStateFlow<List<SoccerField>>(emptyList())
    val fields: StateFlow<List<SoccerField>> = _fields

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private val _fieldsWithDistance = MutableStateFlow<List<Pair<SoccerField, Double>>>(emptyList())
    val fieldsWithDistance: StateFlow<List<Pair<SoccerField, Double>>> = _fieldsWithDistance

    fun loadFields() {
        viewModelScope.launch {
            val fieldsList = repository.getSoccerFields()
            _fields.value = fieldsList
            sortFieldsByDistance()
        }
    }

    fun updateUserLocation(location: Location) {
        _userLocation.value = location
        sortFieldsByDistance()
    }

    private fun sortFieldsByDistance() {
        _userLocation.value?.let { userLoc ->
            val sortedList = _fields.value.map { field ->
                val fieldLocation = Location("").apply {
                    latitude = field.latitude
                    longitude = field.longitude
                }
                val distanceKm = userLoc.distanceTo(fieldLocation) / 1000.0 // המרחק בקילומטרים
                field to distanceKm
            }.sortedBy { it.second }
            _fieldsWithDistance.value = sortedList
        }
    }
}
