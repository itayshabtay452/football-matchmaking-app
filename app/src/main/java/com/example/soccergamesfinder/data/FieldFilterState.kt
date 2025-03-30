package com.example.soccergamesfinder.data

data class FieldFilterState(
    val city: String? = null,
    val lighting: Boolean = false,
    val size: String? = null,
    val maxDistanceKm: Double = 10.0
)

