package com.example.soccergamesfinder.model

import com.google.firebase.Timestamp

/**
 * Represents the current status of a game.
 */
enum class GameStatus {
    /** Game is open and has available spots */
    OPEN,

    /** Game has reached maximum capacity */
    FULL,

    /** Game has already ended */
    ENDED
}

/**
 * Data class representing a football match.
 *
 * @property id Unique game ID (Firestore document ID)
 * @property fieldId ID of the field where the game takes place
 * @property creatorId ID of the user who created the game
 * @property startTime Game start timestamp
 * @property endTime Game end timestamp
 * @property maxPlayers Maximum number of players allowed in the game
 * @property joinedPlayers List of user IDs who have joined the game
 * @property fieldName (Optional) Field name – used for display only
 * @property fieldAddress (Optional) Field address – used for display only
 * @property creatorName (Optional) Creator's name – used for display only
 * @property description (Optional) Game description (e.g., skill level, rules)
 * @property players (Optional) List of player names or IDs (deprecated?)
 * @property status Local-only computed game status (OPEN / FULL / ENDED)
 * @property rating (Optional) Average rating for the game (future use)
 */
data class Game(
    val id: String = "",
    val fieldId: String = "",
    val creatorId: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),  // Format: "yyyy-MM-dd HH:mm"
    val maxPlayers: Int = 10,
    val joinedPlayers: List<String> = emptyList(),

    // Display-only fields
    val fieldName: String? = null,
    val fieldAddress: String? = null,
    val creatorName: String? = null,

    val description: String? = null,
    val players: List<String>? = null, // Consider deprecating if redundant

    // Not stored in Firestore – calculated locally
    val status: GameStatus = GameStatus.OPEN,
    val rating: Double? = null
)
