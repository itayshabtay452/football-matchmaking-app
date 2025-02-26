package com.example.soccergamesfinder.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FieldsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FieldsRepository()

    private val _allFields = MutableStateFlow<List<Field>>(emptyList())
    val allFields: StateFlow<List<Field>> = _allFields

    private val _filteredFields = MutableStateFlow<List<Field>>(emptyList())
    val filteredFields: StateFlow<List<Field>> = _filteredFields

    private val _selectedSize = MutableStateFlow<String?>(null)
    val selectedSize: StateFlow<String?> = _selectedSize

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType

    private val _hasLighting = MutableStateFlow(false)
    val hasLighting: StateFlow<Boolean> = _hasLighting

    private val _paid = MutableStateFlow(false)
    val paid: StateFlow<Boolean> = _paid

    fun loadFields(userLocation: Location?) {
        viewModelScope.launch {
            val fields = repository.getFields(userLocation)
            _allFields.value = fields
            _filteredFields.value = fields
        }
    }

    fun setFilter(size: String?, type: String?, lighting: Boolean, paidField: Boolean) {
        _selectedSize.value = size
        _selectedType.value = type
        _hasLighting.value = lighting
        _paid.value = paidField
        applyFilters()
    }

    fun getFieldById(fieldId: String): Field? {
        return _allFields.value.find { it.id == fieldId }
    }

    fun applyFilters() {
        _filteredFields.value = _allFields.value.filter { field ->
            (selectedSize.value == null || field.fieldSize == selectedSize.value) &&
                    (selectedType.value == null || field.fieldType == selectedType.value) &&
                    (!hasLighting.value || field.hasLighting) && (!paid.value || !field.paid) // עכשיו זה מסנן נכון
        }
    }


    fun clearFilters() {
        _selectedSize.value = null
        _selectedType.value = null
        _hasLighting.value = false
        _paid.value = false
        _filteredFields.value = _allFields.value
    }
}
