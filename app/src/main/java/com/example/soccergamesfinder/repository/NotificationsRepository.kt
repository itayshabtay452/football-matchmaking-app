// NotificationsRepository.kt
package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun listenToNotifications(userId: String): Flow<List<Notification>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val notifications = snapshot.toObjects(Notification::class.java)
                trySend(notifications)
            }

        awaitClose { listener.remove() }
    }

    suspend fun addNotification(userId: String, item: Notification): Result<Unit> {
        return try {
            val docId = item.id.ifBlank { UUID.randomUUID().toString() }
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(docId)
                .set(item.copy(id = docId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNotification(userId: String, notificationId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAllNotificationsAsRead(userId: String) {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .get()
            .await()

        println("Marking ${snapshot.size()} notifications as read for user $userId")


        val batch = firestore.batch()
        for (doc in snapshot.documents) {
            batch.update(doc.reference, "isRead", true)
        }
        batch.commit().await()
    }

} 
