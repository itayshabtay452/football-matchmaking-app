package com.example.soccergamesfinder.data

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
    val birthDate: String? = null
)
