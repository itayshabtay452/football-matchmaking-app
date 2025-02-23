package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.FieldModel
import com.example.soccergamesfinder.data.FieldRepository
import com.example.soccergamesfinder.ui.utils.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FieldsViewModel : ViewModel() {

    private val repository = FieldRepository()

    private val _fields = MutableStateFlow<List<FieldModel>>(emptyList())
    val fields: StateFlow<List<FieldModel>> = _fields

    fun loadFields(userLatitude: Double, userLongitude: Double) {
        viewModelScope.launch {
            val loadedFields = repository.getAllFields()

            // מיון המגרשים לפי המרחק מהמשתמש
            val sortedFields = loadedFields.sortedBy { field ->
                LocationUtils.calculateDistance(
                    userLatitude, userLongitude,
                    field.latitude, field.longitude
                )
            }

            // עדכון הנתונים הזורמים ל-UI
            _fields.value = sortedFields
        }
    }
}
