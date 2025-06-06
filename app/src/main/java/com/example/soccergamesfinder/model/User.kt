package com.example.soccergamesfinder.model

/**
 * Data class representing a user profile in the app.
 *
 * @property id Unique user ID (Firestore document ID)
 * @property fullName Full name of the user (from Google account)
 * @property nickname Chosen nickname shown in the app
 * @property latitude Latitude of user's current or home location
 * @property longitude Longitude of user's current or home location
 * @property profileImageUrl (Optional) URL to the user's profile picture
 * @property fieldsFollowed List of field IDs the user follows
 * @property gamesFollowed List of game IDs the user follows
 * @property preferredDays Days the user prefers to play (e.g., ["Sunday", "Wednesday"])
 * @property startHour (Optional) Preferred start hour (e.g., 17 for 17:00)
 * @property endHour (Optional) Preferred end hour
 * @property birthDate (Optional) User's birth date as string (e.g., "1992-04-15")
 * @property address (Optional) Human-readable address or city name
 */
data class User(
    val id: String = "",
    val fullName: String = "",
    val nickname: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val profileImageUrl: String? = null,
    val fieldsFollowed: List<String> = emptyList(),
    val gamesFollowed: List<String> = emptyList(),
    val preferredDays: List<String> = emptyList(),
    val startHour: Int? = null,
    val endHour: Int? = null,
    val birthDate: String? = null,
    val address: String? = null
)
