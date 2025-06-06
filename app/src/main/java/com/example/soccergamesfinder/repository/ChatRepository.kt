package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repository responsible for handling chat messages within a game.
 *
 * Uses Firestore subcollections to:
 * - Listen to live message updates
 * - Send new messages
 *
 * Injected using Hilt with a shared [FirebaseFirestore] instance.
 */
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Starts listening to new chat messages in a given game.
     *
     * Messages are ordered by timestamp in ascending order.
     * This function sets up a real-time snapshot listener.
     *
     * @param gameId ID of the game to listen to
     * @param onChange Called when messages are updated (added/removed/changed)
     * @param onError Called when a Firestore error occurs
     * @return ListenerRegistration object to stop listening when needed
     */
    fun listenToMessages(
        gameId: String,
        onChange: (List<Message>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return firestore.collection("games")
            .document(gameId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.toObjects(Message::class.java)
                    onChange(messages)
                }
            }
    }

    /**
     * Sends a chat message to the specified game.
     *
     * Message is stored in the game's `messages` subcollection under its ID.
     *
     * @param gameId ID of the game
     * @param message Message to send
     * @return [Result.success] if sent successfully, or [Result.failure] with the exception
     */
    suspend fun sendMessage(gameId: String, message: Message): Result<Unit> {
        return try {
            val ref = firestore.collection("games")
                .document(gameId)
                .collection("messages")
                .document(message.id)

            ref.set(message).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
