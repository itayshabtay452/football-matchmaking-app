package com.example.soccergamesfinder.model

/**
 * Data class representing a football field.
 *
 * @property id Unique identifier of the field (Firestore document ID)
 * @property name Display name of the field
 * @property address Physical address of the field
 * @property size Optional size descriptor (e.g., "Small", "Medium", "Large")
 * @property lighting Whether the field has lighting for night games
 * @property latitude Optional latitude coordinate
 * @property longitude Optional longitude coordinate
 * @property games List of game IDs scheduled at this field
 * @property rating Optional average user rating for the field
 * @property distance (Client-side only) Distance from the current user in kilometers
 */
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

    // Not stored in Firestore – calculated on client
    val distance: Double? = null      // distance from current user in km
)
