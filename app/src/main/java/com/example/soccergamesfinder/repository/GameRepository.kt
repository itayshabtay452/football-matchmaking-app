package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val gamesCollection = firestore.collection("games")

    /**
     * Retrieves all games from Firestore.
     */
    suspend fun getAllGames(): List<Game> {
        return try {

            val snapshot = gamesCollection.get().await()

            val games = snapshot.toObjects(Game::class.java)

            games
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    /**
     * Retrieves games for a specific field.
     */
    suspend fun getGamesByFieldId(fieldId: String): List<Game> {
        return try {
            val snapshot = gamesCollection
                .whereEqualTo("fieldId", fieldId)
                .get()
                .await()
            snapshot.toObjects(Game::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Retrieves games created or joined by the current user.
     */
    suspend fun getGamesForCurrentUser(): List<Game> {
        val userId = auth.currentUser?.uid ?: return emptyList()

        return try {
            val snapshot = gamesCollection
                .whereArrayContains("joinedPlayers", userId)
                .get()
                .await()
            snapshot.toObjects(Game::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Creates a new game in Firestore.
     */
    suspend fun createGame(game: Game): Result<Unit> {
        return try {
            gamesCollection.document(game.id).set(game).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adds a player to a game, without checking any business logic.
     */
    suspend fun joinGame(gameId: String, userId: String): Result<Unit> {
        return try {
            val gameRef = gamesCollection.document(gameId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(gameRef)
                val game = snapshot.toObject(Game::class.java) ?: return@runTransaction
                val updatedPlayers = game.joinedPlayers + userId
                transaction.update(gameRef, "joinedPlayers", updatedPlayers)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Leaves the current user from a game.
     */
    suspend fun leaveGame(gameId: String): Result<Unit> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Not authenticated"))

        return try {
            val gameRef = gamesCollection.document(gameId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(gameRef)
                val game = snapshot.toObject(Game::class.java) ?: return@runTransaction
                val updatedPlayers = game.joinedPlayers.filter { it != userId }
                transaction.update(gameRef, "joinedPlayers", updatedPlayers)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGame(gameId: String): Result<Unit> {
        return try {
            gamesCollection.document(gameId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves a game by its ID.
     */
    suspend fun getGameById(gameId: String): Game? {
        return try {
            val snapshot = gamesCollection.document(gameId).get().await()
            snapshot.toObject(Game::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateGameStatus(gameId: String, status: GameStatus) {
        gamesCollection.document(gameId).update("status", status.name).await()
    }


}
