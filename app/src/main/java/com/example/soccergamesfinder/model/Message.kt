package com.example.soccergamesfinder.model

/**
 * Data class representing a single chat message within a game.
 *
 * @property id Unique message ID (Firestore document ID)
 * @property gameId ID of the game the message belongs to
 * @property senderId ID of the user who sent the message
 * @property senderName Display name of the sender
 * @property text The content of the message
 * @property timestamp Time the message was sent (Unix time in milliseconds)
 */
data class Message(
    val id: String = "",
    val gameId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
