package com.example.soccergamesfinder.data

data class FieldModel(
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val hasLighting: Boolean = false,           // האם יש תאורה
    val requiresOwnerApproval: Boolean = false, // האם צריך לתאם עם בעל המגרש
    val isPaid: Boolean = false,                // האם זה עולה כסף
    val fieldSize: String = "",                 // כמה על כמה (למשל "5x5")
    val fieldType: String = ""                  // סוג המגרש (למשל: דשא סינטטי, בטון, דשא טבעי)
)
