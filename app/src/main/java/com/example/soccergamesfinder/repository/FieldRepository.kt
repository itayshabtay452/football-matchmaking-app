package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.model.Field
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repository for accessing and managing football fields in Firestore.
 *
 * Supports:
 * - Realtime listeners for all fields or a specific field
 * - Fetching a field by ID
 * - Removing a game from a field's list
 * - Submitting new field suggestions (moderated separately)
 *
 * Injected via Hilt with a shared [FirebaseFirestore] instance.
 */
class FieldRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    /**
     * Retrieves a single field by its Firestore document ID.
     *
     * @param id The field's document ID
     * @return [Field] object if found, null otherwise
     */
    suspend fun getFieldById(id: String): Field? {
        return try {
            firestore.collection("facilities")
                .document(id)
                .get()
                .await()
                .toObject(Field::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Removes a game ID from a field's list of games in Firestore.
     *
     * @param fieldId The ID of the field
     * @param gameId The ID of the game to remove
     * @return [Result.success] if removed successfully, or [Result.failure] with error
     */
    suspend fun removeGameFromField(fieldId: String, gameId: String): Result<Unit> {
        return try {
            val fieldRef = firestore.collection("facilities").document(fieldId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(fieldRef)
                val field = snapshot.toObject(Field::class.java) ?: return@runTransaction
                val updatedGames = field.games.filterNot { it == gameId }
                transaction.update(fieldRef, "games", updatedGames)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Listens to all fields in Firestore and observes real-time updates.
     *
     * @param onChange Called when the list of fields changes
     * @param onError Called when a Firestore error occurs
     * @return ListenerRegistration to allow stopping the listener
     */
    fun listenToFields(
        onChange: (List<Field>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return firestore.collection("facilities")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val fields = snapshot.toObjects(Field::class.java)
                    onChange(fields)
                }
            }
    }

    /**
     * Listens to real-time updates for a single field by ID.
     *
     * @param fieldId The ID of the field to observe
     * @param onChange Called when the field data changes
     * @param onError Called when a Firestore error occurs
     * @return ListenerRegistration to allow stopping the listener
     */
    fun listenToFieldById(
        fieldId: String,
        onChange: (Field) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return firestore.collection("facilities")
            .document(fieldId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val field = snapshot?.toObject(Field::class.java)
                if (field != null) {
                    onChange(field)
                } else {
                    onError(Exception("Field not found"))
                }
            }
    }

    /**
     * Submits a new field suggestion to a separate collection for review.
     *
     * Used when a user suggests a field that is not yet in the system.
     *
     * @param field Field object to submit
     * @return [Result.success] if stored successfully, or [Result.failure] with exception
     */
    suspend fun submitFieldSuggestion(field: Field): Result<Unit> {
        return try {
            firestore.collection("field_suggestions")
                .document(field.id)
                .set(field)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
