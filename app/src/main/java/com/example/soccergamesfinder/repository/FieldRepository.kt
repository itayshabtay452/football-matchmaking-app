package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FieldRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * Retrieves all fields from Firestore.
     * This returns the raw data without calculating distance.
     */
    suspend fun getAllFields(): List<Field> {
        return try {
            val snapshot = firestore.collection("facilities").get().await()

            val fields = snapshot.toObjects(Field::class.java)

            fields
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    suspend fun getFieldById(id: String): Field? {
        return try {
            firestore.collection("facilities").document(id).get().await().toObject(Field::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addGameToField(fieldId: String, gameId: String): Result<Unit> {
        return try {
            val fieldRef = firestore.collection("facilities").document(fieldId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(fieldRef)
                val field = snapshot.toObject(Field::class.java) ?: return@runTransaction
                val updatedGames = field.games + gameId
                transaction.update(fieldRef, "games", updatedGames)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
