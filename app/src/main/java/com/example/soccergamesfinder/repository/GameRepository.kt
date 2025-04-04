package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Game
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getGamesForField(fieldId: String): List<Game> {

        return try {
            val snapshot = firestore.collection("games")
                .whereEqualTo("fieldId", fieldId)
                .orderBy("startTime")
                .get()
                .await()

            val gamesList = snapshot.documents.mapNotNull { it.toObject(Game::class.java) }

            gamesList
        } catch (e: Exception) {
            println("⚠️ שגיאה בשליפת משחקים: ${e.message}")
            emptyList()
        }
    }

    suspend fun getUserGames(userId: String): List<Game> {
        return try {
            val snapshot = firestore.collection("games")
                .whereArrayContains("players", userId)
                .get()
                .await()

            snapshot.toObjects(Game::class.java)
        } catch (e: Exception) {
            println("⚠️ שגיאה בשליפת משחקי המשתמש: ${e.message}")
            emptyList()
        }
    }


    suspend fun createGame(game: Game): Boolean {
        return try {
            val newGameRef = firestore.collection("games").document()
            newGameRef.set(game.copy(id = newGameRef.id)).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun deleteGame(gameId: String): Boolean {
        return try {
            firestore.collection("games").document(gameId).delete().await()
            true
        } catch (e: Exception) {
            println("⚠️ שגיאה במחיקת המשחק: ${e.message}")
            false
        }
    }

    suspend fun updatePlayersList(gameId: String, updatedPlayers: List<String>): Boolean {
        return try {
            firestore.collection("games").document(gameId)
                .update("players", updatedPlayers)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend  fun getGameById(gameId: String): Game? {
        return try {
            val snapshot = firestore.collection("games").document(gameId).get().await()
            val game = snapshot.toObject(Game::class.java)
            game
        } catch (e: Exception) {
            null
        }
    }

}