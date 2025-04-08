package com.example.soccergamesfinder.data

data class User(
    val id: String = "",
    val fullName: String = "",
    val nickname: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val profileImageUrl: String? = null
)
