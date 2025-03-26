package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun listenToMessages(gameId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listenerRegistration: ListenerRegistration = firestore.collection("games")
            .document(gameId).collection("massage")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot.toObjects(ChatMessage::class.java)
                trySend(messages)
        }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun sendMessage(gameId: String, message: ChatMessage) {
        firestore.collection("games").document(gameId).collection("massage")
            .add(message).await()
    }



}