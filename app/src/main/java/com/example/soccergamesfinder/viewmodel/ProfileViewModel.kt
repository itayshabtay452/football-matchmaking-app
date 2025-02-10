package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileFormState(
    val firstName: String = "",
    val lastName: String = "",
    val selectedAge: String = "",
    val nickName: String = "",
    val location: String = "",
    val imageUri: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val ageError: String? = null,
    val nickNameError: String? = null,
    val locationError: String? = null,
    val imageUriError: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _profileFormState = MutableStateFlow(ProfileFormState())
    val profileFormState: StateFlow<ProfileFormState> = _profileFormState

    private val _profileSaveSuccess = MutableStateFlow(false)
    val profileSaveSuccess: StateFlow<Boolean> = _profileSaveSuccess

    fun onFirstNameChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            firstName = newValue,
            firstNameError = if (newValue.isBlank()) "יש להזין שם פרטי" else null
        )
    }

    fun onLastNameChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            lastName = newValue,
            lastNameError = if (newValue.isBlank()) "יש להזין שם משפחה" else null
        )
    }

    fun onAgeChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            selectedAge = newValue,
            ageError = if (newValue.isBlank()) "יש לבחור גיל" else null
        )
    }

    fun onNickNameChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            nickName = newValue,
            nickNameError = if (newValue.isBlank()) "יש להזין כינוי" else null
        )
    }

    fun onLocationChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            location = newValue,
            locationError = if (newValue.isBlank()) "יש להזין מיקום" else null
        )
    }

    fun onImageUriChanged(newValue: String) {
        _profileFormState.value = _profileFormState.value.copy(
            imageUri = newValue,
            imageUriError = if (newValue.isBlank()) "יש לבחור תמונה" else null
        )
    }

    fun saveProfile() {
        val state = _profileFormState.value
        var valid = true

        if (state.firstName.isBlank()) {
            _profileFormState.value = state.copy(firstNameError = "יש להזין שם פרטי")
            valid = false
        }
        if (state.lastName.isBlank()) {
            _profileFormState.value = state.copy(lastNameError = "יש להזין שם משפחה")
            valid = false
        }
        if (state.selectedAge.isBlank()) {
            _profileFormState.value = state.copy(ageError = "יש לבחור גיל")
            valid = false
        }
        if (state.nickName.isBlank()) {
            _profileFormState.value = state.copy(nickNameError = "יש להזין כינוי")
            valid = false
        }
        if (state.location.isBlank()) {
            _profileFormState.value = state.copy(locationError = "יש להזין מיקום")
            valid = false
        }
        if (state.imageUri.isBlank()) {
            _profileFormState.value = state.copy(imageUriError = "יש לבחור תמונה")
            valid = false
        }

        if (valid) {
            _profileSaveSuccess.value = true
        }
    }
    fun resetProfileSaveSuccess() {
        _profileSaveSuccess.value = false
    }
}
