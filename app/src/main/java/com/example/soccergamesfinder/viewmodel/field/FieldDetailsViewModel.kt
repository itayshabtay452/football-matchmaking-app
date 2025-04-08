package com.example.soccergamesfinder.viewmodel.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FieldDetailsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val field: Field? = null
)

@HiltViewModel
class FieldDetailsViewModel @Inject constructor(
    private val fieldRepository: FieldRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FieldDetailsState())
    val state: StateFlow<FieldDetailsState> = _state.asStateFlow()

    fun loadFieldById(fieldId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val field = fieldRepository.getFieldById(fieldId)
                if (field != null) {
                    _state.value = FieldDetailsState(
                        isLoading = false,
                        field = field
                    )
                } else {
                    showError("המגרש לא נמצא")
                }
            } catch (e: Exception) {
                showError("שגיאה בטעינת המגרש")
            }
        }
    }

    private fun showError(message: String) {
        _state.value = _state.value.copy(isLoading = false, error = message)
    }
}
