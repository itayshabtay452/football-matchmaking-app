package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// הגדרת מצבי ה-UI עבור תהליכי האימות והרישום
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> get() = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.signIn(email, password)
            _uiState.value = if (result.isSuccess)
                AuthUiState.Success
            else
                AuthUiState.Error(result.exceptionOrNull()?.localizedMessage ?: "אירעה שגיאה בהתחברות")
        }
    }

    // פונקציה לרישום משתמש עם כל הנתונים – אימייל, סיסמה ופרטי פרופיל
    fun register(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.register(
                email = email,
                password = password
            )
            _uiState.value = if (result.isSuccess)
                AuthUiState.Success
            else
                AuthUiState.Error(result.exceptionOrNull()?.localizedMessage ?: "שגיאה ברישום")
        }
    }

    // פונקציה להשלמת פרופיל המשתמש (במידה והרישום הראשוני בוצע רק עם אימייל וסיסמה)
    fun completeProfile(
        fullName: String,
        location: String,
        travelDistance: String,
        freeHours: String,
        skillLevel: String,
        imageUri: String
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            val result = repository.completeProfile(
                fullName = fullName,
                location = location,
                travelDistance = travelDistance,
                freeHours = freeHours,
                skillLevel = skillLevel,
                imageUri = imageUri
            )
            _uiState.value = if (result.isSuccess)
                AuthUiState.Success
            else
                AuthUiState.Error(result.exceptionOrNull()?.localizedMessage ?: "שגיאה בהשלמת הפרופיל")
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
