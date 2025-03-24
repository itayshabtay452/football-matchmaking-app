package com.example.soccergamesfinder.data

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
