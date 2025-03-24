package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.FieldFilterState
import com.example.soccergamesfinder.repository.FieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class FieldViewModel @Inject constructor(
    private val repository: FieldRepository
) : ViewModel() {

    private val _fields = MutableStateFlow<List<Field>>(emptyList())
    val fields: StateFlow<List<Field>> get() = _fields

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _field = MutableStateFlow<Field?>(null)
    val field: StateFlow<Field?> get() = _field.asStateFlow()

    private val _filterState = MutableStateFlow(FieldFilterState())
    val filterState: StateFlow<FieldFilterState> get() = _filterState.asStateFlow()

    private val batchSize = 20
    private var lastIndex = 0
    private var allFields = listOf<Field>() // כל המגרשים שנשלפו


    fun loadNearbyFields(latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null){
            return
        }
        viewModelScope.launch {
            _isLoading.value = true

            val fetchedFields = repository.getFieldsInArea(latitude, longitude)


            allFields = fetchedFields?.map { newField ->
                val distance = newField.latitude?.let {
                    newField.longitude?.let { it1 ->
                        calculateDistance(latitude, longitude,
                            it, it1
                        )
                    }
                }
                newField.copy(distance = distance) // מעדכן את השדה distance
            }?.sortedBy { it.distance }!! // ממיין מהקרוב לרחוק

            lastIndex = 0
            loadMoreFields()
            _isLoading.value = false
            }
        }

    fun loadField(fieldId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _field.value = repository.getFieldById(fieldId)
            _isLoading.value = false
        }
    }

    fun loadMoreFields() {
        val nextIndex = (lastIndex + batchSize).coerceAtMost(allFields.size)
        _fields.value = allFields.subList(0, nextIndex)
        lastIndex = nextIndex
    }

    fun applyFilters() {
        val currentFilterState = _filterState.value

        val filteredFields = allFields.filter { field ->
            (!currentFilterState.lighting || field.lighting == "כן") &&
                    (!currentFilterState.parking || field.parking == "כן") &&
                    (!currentFilterState.fencing || field.fencing == "כן") &&
                    (currentFilterState.nameQuery.isBlank() || field.name?.contains(currentFilterState.nameQuery, ignoreCase = true) == true) &&
                    (currentFilterState.size == null || field.size == currentFilterState.size) &&
                    (currentFilterState.maxDistanceKm == null || field.distance != null && field.distance <= currentFilterState.maxDistanceKm)
        }

        _fields.value = filteredFields
    }

    fun updateLightingFilter(value: Boolean) {
        _filterState.value = _filterState.value.copy(lighting = value)
        applyFilters()
    }

    fun updateParkingFilter(value: Boolean) {
        _filterState.value = _filterState.value.copy(parking = value)
        applyFilters()
    }

    fun updateFencingFilter(value: Boolean) {
        _filterState.value = _filterState.value.copy(fencing = value)
        applyFilters()
    }

    fun updateNameQuery(query: String) {
        _filterState.value = _filterState.value.copy(nameQuery = query)
        applyFilters()
    }

    fun updateSizeFilter(size: String?) {
        _filterState.value = _filterState.value.copy(size = size)
        applyFilters()
    }

    fun updateMaxDistanceKm(distance: Double?) {
        _filterState.value = _filterState.value.copy(maxDistanceKm = distance)
        applyFilters()
    }


    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371 // רדיוס כדור הארץ בק"מ
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // מרחק בק"מ
    }
}