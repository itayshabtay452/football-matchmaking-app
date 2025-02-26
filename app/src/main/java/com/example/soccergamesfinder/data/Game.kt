package com.example.soccergamesfinder.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Game(
    @get:PropertyName("fieldId") @set:PropertyName("fieldId") var fieldId: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("timeRange") @set:PropertyName("timeRange") var timeRange: String = "",
    @get:PropertyName("createdByUserId") @set:PropertyName("createdByUserId") var createdByUserId: String = "",
    @get:PropertyName("maxPlayers") @set:PropertyName("maxPlayers") var maxPlayers: Int = 10,
    @get:PropertyName("players") @set:PropertyName("players") var players: List<String> = emptyList(), // רשימת המשתתפים
    @get:Exclude var id: String = ""
)


fun DocumentSnapshot.toGame(): Game? {
    return try {
        val game = this.toObject(Game::class.java)
        game?.id = this.id
        game
    } catch (e: Exception) {
        null
    }
}
