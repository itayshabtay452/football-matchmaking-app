package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.model.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Repository responsible for managing user notifications stored in Firestore.
 */
class NotificationsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Listens in real-time to changes in a user's notifications.
     * Emits an updated list of notifications ordered by timestamp (latest first).
     *
     * @param userId The ID of the user to listen for notifications.
     * @return Flow emitting the list of notifications.
     */
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

    /**
     * Adds a new notification for the specified user.
     *
     * @param userId The ID of the user.
     * @param item The notification to be added.
     * @return Result indicating success or failure.
     */
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

    /**
     * Deletes a specific notification for a user.
     *
     * @param userId The ID of the user.
     * @param notificationId The ID of the notification to delete.
     * @return Result indicating success or failure.
     */
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

    /**
     * Marks all unread notifications for a user as read.
     *
     * @param userId The ID of the user.
     */
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
