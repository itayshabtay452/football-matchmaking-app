package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.FieldFilterState
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.utils.FieldFilterManager
import com.example.soccergamesfinder.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FieldListUiState(
    val isLoading: Boolean = false,
    val fields: List<Field> = emptyList(),
    val filterState: FieldFilterState = FieldFilterState()
)

@HiltViewModel
class FieldListViewModel @Inject constructor(
    private val repository: FieldRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FieldListUiState())
    val uiState: StateFlow<FieldListUiState> = _uiState.asStateFlow()

    private val batchSize = 20
    private var lastIndex = 0
    private var allFields = listOf<Field>()

    fun loadNearbyFields(latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val fetchedFields = repository.getFieldsInArea(latitude, longitude)

            allFields = fetchedFields?.map { field ->
                val distance = field.latitude?.let {
                    field.longitude?.let { lon ->
                        LocationUtils.calculateDistance(latitude, longitude, it, lon)
                    }
                }
                field.copy(distance = distance)
            }?.sortedBy { it.distance } ?: emptyList()

            lastIndex = 0
            applyFiltersAndLoad()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun loadMoreFields() {
        applyFiltersAndLoad()
    }

    private fun applyFiltersAndLoad() {
        val filter = _uiState.value.filterState
        val filtered = FieldFilterManager.applyFilters(allFields, filter)

        val nextIndex = (lastIndex + batchSize).coerceAtMost(filtered.size)
        val newFields = filtered.subList(0, nextIndex)

        _uiState.value = _uiState.value.copy(fields = newFields)
        lastIndex = nextIndex
    }

    fun updateLighting(value: Boolean) {
        val currentFilter = _uiState.value.filterState.copy(lighting = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }


    fun updateParking(value: Boolean) {
        val currentFilter = _uiState.value.filterState.copy(parking = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }

    fun updateFencing(value: Boolean) {
        val currentFilter = _uiState.value.filterState.copy(fencing = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }

    fun updateNameQuery(value: String) {
        val currentFilter = _uiState.value.filterState.copy(nameQuery = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }

    fun updateSize(value: String?) {
        val currentFilter = _uiState.value.filterState.copy(size = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }

    fun updateMaxDistance(value: Double?) {
        val currentFilter = _uiState.value.filterState.copy(maxDistanceKm = value)
        _uiState.value = _uiState.value.copy(filterState = currentFilter)
        lastIndex = 0
        applyFiltersAndLoad()
    }
}
