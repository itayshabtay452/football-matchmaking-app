package com.example.soccergamesfinder.ui.screens.addfield

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import com.example.soccergamesfinder.services.LocationService
import com.example.soccergamesfinder.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed class UiEvent {
    object NavigateBack : UiEvent()
    data class ShowError(val message: String) : UiEvent()
}

@HiltViewModel
class AddFieldViewModel @Inject constructor(
    private val fieldRepository: FieldRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(AddFieldState())
    val state: StateFlow<AddFieldState> = _state.asStateFlow()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: AddFieldEvent) {
        when (event) {
            is AddFieldEvent.NameChanged -> _state.update { it.copy(name = event.value) }
            is AddFieldEvent.AddressChanged -> {
                _state.update { it.copy(address = event.value) }
                if (event.value.length > 5) {
                    resolveAddressToMap()
                }
            }            is AddFieldEvent.FindAddressOnMap -> resolveAddressToMap()
            is AddFieldEvent.MapMarkerMoved -> updateAddressFromMap(event.lat, event.lng)
            is AddFieldEvent.DescriptionChanged -> _state.update { it.copy(description = event.value) }
            is AddFieldEvent.SizeChanged -> _state.update { it.copy(size = event.value) }
            is AddFieldEvent.LightingChanged -> _state.update { it.copy(lighting = event.value) }
            is AddFieldEvent.ImageSelected -> _state.update { it.copy(imageUri = event.uri) }
            is AddFieldEvent.Submit -> submit()
            AddFieldEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun resolveAddressToMap() {
        val address = _state.value.address
        if (address.isBlank()) {
            showError("אנא הזן כתובת תקינה")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = locationService.getLocationFromAddress(address)

            result.onSuccess { (lat, lng) ->
                _state.update {
                    it.copy(
                        mapLatitude = lat,
                        mapLongitude = lng,
                        isMapVisible = true,
                        isLoading = false
                    )
                }
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                showError("כתובת לא נמצאה")
            }
        }
    }

    private fun updateAddressFromMap(lat: Double, lng: Double) {
        viewModelScope.launch {
            val address = locationService.getAddressFromLocation(lat, lng)
            _state.update {
                it.copy(
                    mapLatitude = lat,
                    mapLongitude = lng,
                    latitude = lat,
                    longitude = lng,
                    address = address ?: it.address // אם לא מצא, שומר את הקיים
                )
            }
        }
    }

    private fun submit() {
        val state = _state.value

        val validations = listOf(
            Validators.validateNotBlank(state.name, "שם המגרש חובה"),
            Validators.validateNotBlank(state.address, "כתובת חובה"),
            Validators.validateNotBlank(state.size, "גודל חובה"),
            Validators.validateLocation(state.latitude, state.longitude)
        )

        val firstError = validations.firstOrNull { !it.isValid }
        if (firstError != null) {
            showError(firstError.errorMessage ?: "שגיאה בטופס")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val field = Field(
                id = UUID.randomUUID().toString(),
                name = state.name,
                address = state.address,
                size = state.size,
                lighting = state.lighting,
                latitude = state.latitude,
                longitude = state.longitude
            )

            val result = fieldRepository.submitFieldSuggestion(field)

            _state.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                _uiEvents.send(UiEvent.NavigateBack)
            } else {
                showError("שמירת המגרש נכשלה")
            }
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _uiEvents.send(UiEvent.ShowError(message))
        }
    }
}
