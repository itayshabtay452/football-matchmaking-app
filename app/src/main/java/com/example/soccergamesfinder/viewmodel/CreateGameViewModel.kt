package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateGameViewModel : ViewModel() {

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate

    private val _selectedTimeRange = MutableStateFlow("")
    val selectedTimeRange: StateFlow<String> = _selectedTimeRange

    private val _field = MutableStateFlow<Field?>(null)
    val field: StateFlow<Field?> = _field

    fun setField(field: Field) {
        _field.value = field
    }

    fun setDate(date: String) {
        _selectedDate.value = date
    }

    fun setTimeRange(timeRange: String) {
        _selectedTimeRange.value = timeRange
    }
}
