package com.example.soccergamesfinder.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class GameRepository {
    private val firestore = Firebase.firestore
    private val gamesCollection = firestore.collection("games")

    suspend fun createGame(game: Game): Boolean {
        return try {
            gamesCollection.add(game).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isGameSlotTaken(fieldId: String, date: String, timeRange: String): Boolean {
        return try {
            val querySnapshot = gamesCollection
                .whereEqualTo("fieldId", fieldId)
                .whereEqualTo("date", date)
                .whereEqualTo("timeRange", timeRange)
                .get()
                .await()

            !querySnapshot.isEmpty // אם יש תוצאה - המשבצת כבר תפוסה
        } catch (e: Exception) {
            false // במקרה של שגיאה נניח שהמשבצת לא תפוסה
        }
    }

    suspend fun hasUserGameOnDate(userId: String, date: String): Boolean {
        return try {
            val querySnapshot = gamesCollection
                .whereEqualTo("createdByUserId", userId)
                .whereEqualTo("date", date)
                .get()
                .await()

            !querySnapshot.isEmpty // אם יש תוצאה - למשתמש כבר יש משחק באותו יום
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllGames(): List<Game> {
        return try {
            val querySnapshot = gamesCollection.get().await()
            querySnapshot.documents.mapNotNull { it.toGame() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun joinGame(gameId: String, userId: String): Boolean {
        return try {
            val gameRef = gamesCollection.document(gameId)
            val snapshot = gameRef.get().await()
            val game = snapshot.toGame()

            if (game != null && !game.players.contains(userId) && game.players.size < game.maxPlayers) {
                val updatedPlayers = game.players + userId
                gameRef.update("players", updatedPlayers).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }




}
