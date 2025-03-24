package com.example.soccergamesfinder.data

data class FieldFilterState(
    val lighting: Boolean = false,
    val parking: Boolean = false,
    val fencing: Boolean = false,
    val nameQuery: String = "",
    val size: String? = null,         // לדוגמה: "קטן", "בינוני", "גדול"
    val maxDistanceKm: Double? = null // לדוגמה: 3.5
)
