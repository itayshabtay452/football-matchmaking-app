package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FieldRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * Retrieves all fields from Firestore.
     * This returns the raw data without calculating distance.
     */

    suspend fun getFieldById(id: String): Field? {
        return try {
            firestore.collection("facilities").document(id).get().await().toObject(Field::class.java)
        } catch (e: Exception) {
            null
        }
    }

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

    fun listenToFields(onChange: (List<Field>) -> Unit, onError: (Throwable) -> Unit): ListenerRegistration {
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
                    onError(Exception("המגרש לא נמצא"))
                }
            }
    }

    suspend fun submitFieldSuggestion(field: Field): Result<Unit> {
        return try {
            // שומר את ההצעה באוסף נפרד
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
