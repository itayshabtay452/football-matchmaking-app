package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.soccergamesfinder.utils.GameValidator
import com.example.soccergamesfinder.utils.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.Timestamp
import javax.inject.Inject

@HiltViewModel
class GameValidationViewModel @Inject constructor() : ViewModel() {

    private val _startTimeValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val startTimeValidation: StateFlow<ValidationResult> get() = _startTimeValidation

    private val _endTimeValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val endTimeValidation: StateFlow<ValidationResult> get() = _endTimeValidation

    private val _validStartTime = MutableStateFlow<Timestamp?>(null)
    val validStartTime: StateFlow<Timestamp?> get() = _validStartTime

    private val _validEndTime = MutableStateFlow<Timestamp?>(null)
    val validEndTime: StateFlow<Timestamp?> get() = _validEndTime

    fun validateStartTime(date: String, time: String) {
        when (val result = GameValidator.convertToTimestamp(date, time)) {
            is ValidationResult.SuccessWithData<*> -> {
                _startTimeValidation.value = ValidationResult.Success
                _validStartTime.value = result.data as Timestamp?
            }
            is ValidationResult.Error -> {
                _startTimeValidation.value = result
                _validStartTime.value = null
            }

            ValidationResult.Success -> TODO()
        }
    }

    fun validateEndTime(startDate: String, startTime: String, endTime: String) {
        val startResult = GameValidator.convertToTimestamp(startDate, startTime)
        val endResult = GameValidator.convertToTimestamp(startDate, endTime)

        if (startResult is ValidationResult.SuccessWithData<*> &&
            endResult is ValidationResult.SuccessWithData<*>) {

            val startTimestamp = startResult.data as? Timestamp
            val endTimestamp = endResult.data as? Timestamp

            if (startTimestamp != null && endTimestamp != null) {
                val validationResult = GameValidator.validateGameTimes(startTimestamp, endTimestamp)
                _endTimeValidation.value = validationResult
                if (validationResult is ValidationResult.Success) {
                    _validEndTime.value = endTimestamp
                } else {
                    _validEndTime.value = null
                }
            } else {
                _endTimeValidation.value = ValidationResult.Error("⏳ יש לבחור שעה חוקית")
            }
        } else {
            _endTimeValidation.value = ValidationResult.Error("⏳ יש לבחור שעה חוקית")
        }
    }


}
