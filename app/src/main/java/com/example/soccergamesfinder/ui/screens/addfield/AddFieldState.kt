// AddFieldState.kt
package com.example.soccergamesfinder.ui.screens.addfield

data class AddFieldState(
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val size: String = "",
    val lighting: Boolean = false,

    // ערכי מיקום שנבחרו סופית
    val latitude: Double? = null,
    val longitude: Double? = null,

    // ערכי מיקום זמניים מתוך המפה (לפני אישור)
    val mapLatitude: Double? = null,
    val mapLongitude: Double? = null,

    // שליטה האם להציג מפה
    val isMapVisible: Boolean = false,

    // סטטוס כללי
    val isLoading: Boolean = false,
    val error: String? = null,
    val submitted: Boolean = false,

    val imageUri: String? = null
)
