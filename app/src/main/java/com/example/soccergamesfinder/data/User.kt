package com.example.soccergamesfinder.data

data class User(
    val age: Int= 0,
    val name: String = "",
    val nickname: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val profileImageUrl: String? = null,
    val games: List<String> = emptyList()
    )