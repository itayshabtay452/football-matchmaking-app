package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
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
