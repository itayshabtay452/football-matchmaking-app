package com.example.soccergamesfinder.data

import com.google.firebase.Timestamp

enum class GameStatus {
    OPEN,
    FULL,
    ENDED
}

data class Game(
    val id: String = "",
    val fieldId: String = "",
    val creatorId: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),  // Format: "yyyy-MM-dd HH:mm"
    val maxPlayers: Int = 10,
    val joinedPlayers: List<String> = emptyList(),
    val fieldName: String? = null,
    val fieldAddress: String? = null,
    val creatorName: String? = null,


    val description: String? = null,
    val players: List<String>? = null,

    // Not stored in Firestore – calculated locally
    val status: GameStatus = GameStatus.OPEN,   // Can be OPEN / FULL / ENDED
    val rating: Double? = null                  // Future: average rating
)
