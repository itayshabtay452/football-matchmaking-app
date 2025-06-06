package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.model.Field
import com.example.soccergamesfinder.model.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repository responsible for managing game-related operations in Firestore.
 *
 * Includes real-time listeners, game creation, joining/leaving games,
 * and managing ended games and relations with fields.
 */
class GameRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val gamesCollection = firestore.collection("games")

    /** Listens to all games in Firestore. */
    fun listenToGames(onChange: (List<Game>) -> Unit, onError: (Throwable) -> Unit): ListenerRegistration {
        return gamesCollection.addSnapshotListener { snapshot, error ->
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

    /** Creates a new game and attaches it to a field in a transaction. */
    suspend fun createGameAndAttachToField(game: Game): Result<Unit> {
        return try {
            val gameRef = gamesCollection.document(game.id)
            val fieldRef = firestore.collection("facilities").document(game.fieldId)

            firestore.runTransaction { transaction ->
                val fieldSnap = transaction.get(fieldRef)
                val field = fieldSnap.toObject(Field::class.java)
                    ?: throw Exception("Field not found")
                val updatedGames = field.games + game.id
                transaction.set(gameRef, game)
                transaction.update(fieldRef, "games", updatedGames)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Returns games for a specific field. */
    suspend fun getGamesByFieldId(fieldId: String): List<Game> {
        return try {
            gamesCollection.whereEqualTo("fieldId", fieldId).get().await().toObjects(Game::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Returns games the current user has joined. */
    suspend fun getGamesForCurrentUser(): List<Game> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return try {
            gamesCollection.whereArrayContains("joinedPlayers", userId).get().await().toObjects(Game::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Returns games joined by a specific user. */
    suspend fun getGamesForUser(userId: String): List<Game> {
        return try {
            gamesCollection.whereArrayContains("joinedPlayers", userId).get().await().toObjects(Game::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Returns ended games for a specific user from the 'ended_games' collection. */
    suspend fun getEndedGamesForUser(userId: String): List<Game> {
        return try {
            firestore.collection("ended_games")
                .whereArrayContains("joinedPlayers", userId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Game::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Adds a player to a game. */
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

    /** Removes the current user from a game. */
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

    /** Deletes a game document. */
    suspend fun deleteGame(gameId: String): Result<Unit> {
        return try {
            gamesCollection.document(gameId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Listens to all games for a given field. */
    fun listenToGamesByFieldId(
        fieldId: String,
        onChange: (List<Game>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return gamesCollection
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

    /** Listens to a single game by ID. */
    fun listenToGameById(
        gameId: String,
        onChange: (Game) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return gamesCollection
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
                    onError(Exception("Game not found"))
                }
            }
    }

    /** Moves a game to the 'ended_games' collection and deletes it from 'games'. */
    suspend fun moveGameToEndedCollection(gameId: String): Result<Unit> {
        return try {
            val gameSnapshot = gamesCollection.document(gameId).get().await()
            if (!gameSnapshot.exists()) return Result.failure(Exception("Game does not exist"))
            val gameData = gameSnapshot.data ?: return Result.failure(Exception("Game has no data"))

            firestore.collection("ended_games").document(gameId).set(gameData).await()
            gamesCollection.document(gameId).delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
