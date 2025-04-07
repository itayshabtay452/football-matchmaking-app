package com.example.soccergamesfinder.data

data class Field(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val size: String? = null,         // e.g., "Small", "Medium", "Large"
    val lighting: Boolean = false,    // whether the field has lighting
    val latitude: Double? = null,
    val longitude: Double? = null,
    val games: List<String> = emptyList(), // list of game IDs
    val rating: Double? = null,       // average rating, e.g., 4.2

    // Client-side only (not stored in Firestore)
    val distance: Double? = null      // distance from current user in km
)