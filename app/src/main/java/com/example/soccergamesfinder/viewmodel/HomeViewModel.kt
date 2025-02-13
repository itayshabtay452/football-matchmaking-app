package com.example.soccergamesfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.AuthRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository = AuthRepository() // ניתן להשתמש ב־DI כדי לספק את ה־repository
) : ViewModel() {

    /**
     * ביצוע התנתקות המשתמש.
     * הפונקציה מפעילה את פעולת ההתנתקות דרך ה־AuthRepository בתוך viewModelScope.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout() // נניח שהפונקציה logout מטפלת בתהליך היציאה מהמערכת
        }
    }
}
