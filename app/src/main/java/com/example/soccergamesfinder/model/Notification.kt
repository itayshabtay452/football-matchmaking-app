package com.example.soccergamesfinder.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

/**
 * Represents a single in-app notification sent to the user.
 *
 * @property id Unique identifier for the notification (can be gameId, fieldId, etc.)
 * @property title Notification title (e.g., "New Game Added")
 * @property message Short description of the event
 * @property timestamp Time when the notification occurred (Firebase Timestamp)
 * @property type Type of the notification (e.g., "game", "field", "chat")
 * @property targetId ID of the related entity (gameId / fieldId / etc.)
 * @property isRead Whether the user has already read the notification
 */
data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val type: String = "game",
    val targetId: String = "",

    @get: PropertyName("isRead")
    val isRead: Boolean = false
)
