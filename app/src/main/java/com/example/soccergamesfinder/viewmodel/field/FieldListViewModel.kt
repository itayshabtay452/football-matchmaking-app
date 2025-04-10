package com.example.soccergamesfinder.viewmodel.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.utils.DistanceCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FieldListViewModel @Inject constructor(
    private val fieldRepository: FieldRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(FieldListState())
    val state: StateFlow<FieldListState> = _state.asStateFlow()

    init {
        loadFieldsWithDistance()
    }

    private fun loadFieldsWithDistance() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val fields = fieldRepository.getAllFields()

            val locationResult = locationService.getCurrentLocation()
            val userLocation = locationResult.getOrNull()

            val updatedFields = if (userLocation != null) {
                fields.map { field ->
                    val distance = DistanceCalculator.calculate(
                        userLat = userLocation.first,
                        userLng = userLocation.second,
                        fieldLat = field.latitude ?: 0.0,
                        fieldLng = field.longitude ?: 0.0
                    )
                    field.copy(distance = distance)
                }.sortedBy { it.distance }
            } else {
                fields
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    fields = updatedFields
                )
            }
        }
    }

    fun updateFieldWithNewGame(fieldId: String, gameId: String) {
        val currentFields = _state.value.fields.toMutableList()
        val index = currentFields.indexOfFirst { it.id == fieldId }

        if (index != -1) {
            val field = currentFields[index]
            val updatedField = field.copy(games = field.games + gameId)
            currentFields[index] = updatedField
            _state.update { it.copy(fields = currentFields) }
        }
    }

    // FieldListViewModel.kt
    fun removeGameFromField(fieldId: String, gameId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = fieldRepository.removeGameFromField(fieldId, gameId)
            if (result.isSuccess) {
                _state.update { current ->
                    val updatedFields = current.fields.map { field ->
                        if (field.id == fieldId) {
                            field.copy(games = field.games.filterNot { it == gameId })
                        } else field
                    }
                    current.copy(fields = updatedFields, isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "שגיאה במחיקת המשחק מהמגרש: ${result.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }
}
