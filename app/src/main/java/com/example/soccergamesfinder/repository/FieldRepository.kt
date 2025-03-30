package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
    suspend fun getFieldsInArea(latitude: Double, longitude: Double): List<Field>? {
        return try {
            val result = firestore.collection("facilities")
                .whereGreaterThanOrEqualTo("latitude", latitude - 0.5)
                .whereLessThanOrEqualTo("latitude", latitude + 0.5)
                .whereGreaterThanOrEqualTo("longitude", longitude - 0.5)
                .whereLessThanOrEqualTo("longitude", longitude + 0.5)
                .get()
                .await()

            result.documents.mapNotNull { doc ->
                doc.toObject(Field::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            println("❌ שגיאה בטעינת מגרשים: ${e.message}")
            null
        }
    }




    suspend fun getFieldById(fieldId: String): Field? {
        return try {
            val doc = firestore.collection("facilities").document(fieldId).get().await()
            val field = doc.toObject(Field::class.java)
            field?.copy(id = doc.id)  // נכניס את ה-id ידנית
        } catch (e: Exception) {
            println("❌ שגיאה בטעינת מגרש: ${e.message}")
            null
        }
    }


}