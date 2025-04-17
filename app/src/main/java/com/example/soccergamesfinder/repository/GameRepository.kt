package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.example.soccergamesfinder.data.Game
import com.example.soccergamesfinder.data.GameStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
    fun listenToGames(onChange: (List<Game>) -> Unit, onError: (Throwable) -> Unit): ListenerRegistration {
        return firestore.collection("games")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val games = snapshot.toObjects(Game::class.java)
                    onChange(games)
                }
            }
    }

    suspend fun createGameAndAttachToField(game: Game): Result<Unit> {
        return try {
            val gameRef = firestore.collection("games").document(game.id)
            val fieldRef = firestore.collection("facilities").document(game.fieldId)

            firestore.runTransaction { transaction ->
                // 🔁 1. קודם כל קראה את המגרש
                val fieldSnap = transaction.get(fieldRef)
                val field = fieldSnap.toObject(Field::class.java)
                    ?: throw Exception("Field not found")

                val updatedGames = field.games + game.id

                // ✅ 2. עכשיו כתוב את המשחק ועדכן את המגרש
                transaction.set(gameRef, game)
                transaction.update(fieldRef, "games", updatedGames)
            }.await()

            println(">>> המשחק נשמר בהצלחה עם ID: ${game.id}")
            Result.success(Unit)

        } catch (e: Exception) {
            println(">>> שמירת המשחק נכשלה: ${e.message}")
            Result.failure(e)
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

    fun listenToGamesByFieldId(
        fieldId: String,
        onChange: (List<Game>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return firestore.collection("games")
            .whereEqualTo("fieldId", fieldId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val games = snapshot.toObjects(Game::class.java)
                    onChange(games)
                } else {
                    onChange(emptyList())
                }
            }
    }

    fun listenToGameById(
        gameId: String,
        onChange: (Game) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return firestore.collection("games")
            .document(gameId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val game = snapshot?.toObject(Game::class.java)
                if (game != null) {
                    onChange(game)
                } else {
                    onError(Exception("המשחק לא נמצא"))
                }
            }
    }



}
