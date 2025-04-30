// NotificationItem.kt
package com.example.soccergamesfinder.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

/**
 * מייצג התראה בודדת לאפליקציה.
 */
data class Notification(
    val id: String = "",            // מזהה ייחודי (למשל gameId או fieldId)
    val title: String = "",         // כותרת התראה
    val message: String = "",       // טקסט קצר המתאר את האירוע

    val timestamp: Timestamp = Timestamp.now(), // חותמת זמן מתי התרחשה ההתראה (millis)
    val type: String = "game",
    val targetId: String = "",
    @get: PropertyName("isRead")
    val isRead: Boolean = false
)

