package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileFormState(
    val firstName: String = "",
    val lastName: String = "",
    val selectedAge: String = "",
    val nickName: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val ageError: String? = null,
    val nickNameError: String? = null,
)

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {
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

    // עדכון לכינוי: בדיקת פורמט (ללא רווחים, רק תווים באנגלית, מספרים ותווים) ובדיקה מול Firebase
    fun onNickNameChanged(newValue: String) {
        // בדיקת פורמט – מאפשרים תווים מ־'!' ועד '~'
        val allowedRegex = "^[\\x21-\\x7E]*$".toRegex()
        val formatError = if (!newValue.matches(allowedRegex)) {
            "הכינוי יכול להכיל אותיות, מספרים ותווים בלבד (ללא רווחים)"
        } else null

        _profileFormState.update {
            it.copy(nickName = newValue, nickNameError = formatError)
        }
    }

    fun saveProfile() {
        val state = _profileFormState.value
        var valid = true

        if (state.firstName.isBlank()) {
            _profileFormState.update { it.copy(firstNameError = "יש להזין שם פרטי") }
            valid = false
        }
        if (state.lastName.isBlank()) {
            _profileFormState.update { it.copy(lastNameError = "יש להזין שם משפחה") }
            valid = false
        }
        if (state.selectedAge.isBlank()) {
            _profileFormState.update { it.copy(ageError = "יש לבחור גיל") }
            valid = false
        }
        if (state.nickName.isBlank()) {
            _profileFormState.update { it.copy(nickNameError = "יש להזין כינוי") }
            valid = false
        }

        if (valid) {
            // ביצוע השמירה במסד הנתונים נעשה בתוך קורוטינה כדי לטפל בפעולות אסינכרוניות
            viewModelScope.launch {
                    // בונים את המפה של הנתונים
                    val data = hashMapOf(
                        "firstName" to state.firstName,
                        "lastName" to state.lastName,
                        "selectedAge" to state.selectedAge,
                        "nickName" to state.nickName
                    )

                    repository.completeProfile(data)

                    _profileSaveSuccess.value = true

            }
        }
    }

    fun resetProfileSaveSuccess() {
        _profileSaveSuccess.value = false
    }

    fun checkNicknameUnique(nickName: String) {
        // רק אם הכינוי אינו ריק ועבר את בדיקת הפורמט
        val allowedRegex = "^[\\x21-\\x7E]+$".toRegex()
        if (nickName.isNotBlank() && nickName.matches(allowedRegex)) {
            viewModelScope.launch {
                val isTaken = repository.isNicknameTaken(nickName)
                if (isTaken) {
                    _profileFormState.update { current ->
                        current.copy(nickNameError = "כינוי זה כבר בשימוש, אנא בחר כינוי אחר")
                    }
                } else {
                    _profileFormState.update { current ->
                        if (current.nickNameError == "כינוי זה כבר בשימוש, אנא בחר כינוי אחר")
                            current.copy(nickNameError = null)
                        else current
                    }
                }
            }
        }
    }
}
