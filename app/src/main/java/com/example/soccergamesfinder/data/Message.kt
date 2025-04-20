package com.example.soccergamesfinder.data

data class Message(
    val id: String = "",
    val gameId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
