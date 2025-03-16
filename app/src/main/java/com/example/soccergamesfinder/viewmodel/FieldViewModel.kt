package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.repository.FieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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


    fun loadFields(reset: Boolean = false) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            if (reset) {
                repository.resetPagination()
                _fields.value = emptyList()
            }
            val newFields = repository.getFields()
            _fields.value += newFields
            _isLoading.value = false
        }
    }


    fun resetFields() {
        repository.resetPagination()
        _fields.value = emptyList()
    }

    fun loadField(fieldId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _field.value = repository.getFieldById(fieldId)
            _isLoading.value = false
        }
    }
}