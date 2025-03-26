package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FieldDetailsUiState {
    object Loading : FieldDetailsUiState()
    data class Success(val field: Field) : FieldDetailsUiState()
    data class Error(val message: String) : FieldDetailsUiState()
}

@HiltViewModel
class FieldDetailsViewModel @Inject constructor(
    private val repository: FieldRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FieldDetailsUiState>(FieldDetailsUiState.Loading)
    val uiState: StateFlow<FieldDetailsUiState> = _uiState

    fun loadField(fieldId: String) {
        viewModelScope.launch {
            _uiState.value = FieldDetailsUiState.Loading
            try {
                val field = repository.getFieldById(fieldId)
                if (field != null) {
                    _uiState.value = FieldDetailsUiState.Success(field)
                } else {
                    _uiState.value = FieldDetailsUiState.Error("מגרש לא נמצא")
                }
            } catch (e: Exception) {
                _uiState.value = FieldDetailsUiState.Error("שגיאה בטעינת מגרש")
            }
        }
    }
}

