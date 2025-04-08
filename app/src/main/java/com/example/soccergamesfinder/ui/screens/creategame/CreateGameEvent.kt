// CreateGameEvent.kt
package com.example.soccergamesfinder.ui.screens.creategame

sealed class CreateGameEvent {

    // תאריך
    data class DateSelected(val value: String) : CreateGameEvent()

    // שעה התחלה / סיום (בחר מתוך רשימה בלבד)
    data class StartTimeSelected(val value: String) : CreateGameEvent()
    data class EndTimeSelected(val value: String) : CreateGameEvent()

    // מספר שחקנים ותיאור
    data class MaxPlayersChanged(val value: Int) : CreateGameEvent()
    data class DescriptionChanged(val value: String) : CreateGameEvent()

    // פעולות כלליות
    object Submit : CreateGameEvent()
    object Cancel : CreateGameEvent()
}
