package com.example.soccergamesfinder.viewmodel.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.utils.DistanceCalculator
import com.google.firebase.firestore.ListenerRegistration
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
        startListeningToFields()
    }


    private var listener: ListenerRegistration? = null

    private fun startListeningToFields() {
        if (listener != null) return // כבר מאזין

        listener = fieldRepository.listenToFields(
            onChange = { fields ->
                viewModelScope.launch {
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

                    _state.update { it.copy(fields = updatedFields, isLoading = false, error = null) }
                }
            },
            onError = { e ->
                _state.update { it.copy(error = e.message ?: "שגיאה בהאזנה למגרשים") }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

}
