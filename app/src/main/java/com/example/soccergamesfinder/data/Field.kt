package com.example.soccergamesfinder.data

data class Field(
    val id: String = "",
    val name: String? = null,
    val address: String? = null,
    val size: String? = null,
    val lighting: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val games: List<String> = emptyList(),  // מזהים של משחקים
    val distance: Double? = null
)
