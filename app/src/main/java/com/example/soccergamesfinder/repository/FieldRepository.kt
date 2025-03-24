package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject
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
                .whereGreaterThanOrEqualTo("latitude", (latitude) - 0.5)
                .whereLessThanOrEqualTo("latitude", latitude + 0.5)
                .whereGreaterThanOrEqualTo("longitude", longitude - 0.5)
                .whereLessThanOrEqualTo("longitude", longitude + 0.5)
                .get().await()


            val fieldList = result.documents.map { doc ->
                Field(
                    id = doc.id,
                    name = doc.getString("שם המתקן") ?: "לא ידוע",
                    size = doc.getString("גודל") ?: "לא ידוע",
                    email = doc.getString("דואל איש קשר"),
                    phone = doc.getString("טלפון איש קשר"),
                    lighting = doc.getString("תאורה קיימת") ?: "לא ידוע",
                    latitude = doc.getDouble("latitude"),
                    longitude = doc.getDouble("longitude"),
                    address = doc.getString("כתובת")

                )
            }
            fieldList
        } catch (e: Exception) {
            println("⚠️ שגיאת Firestore: ${e.message}")
            null
        }
    }



    suspend fun getFieldById(fieldId: String): Field? {
        return try {
            val document = firestore.collection("facilities").document(fieldId).get().await()
            val field = Field(
                id = fieldId,
                name = document.getString("שם המתקן") ?: "לא ידוע",
                address = document.getString("כתובת") ?: "לא ידוע",
                size = document.getString("גודל") ?: "לא ידוע",
                fencing = document.getString("גידור קיים") ?: "לא ידוע",
                email = document.getString("דואל איש קשר") ?: "לא זמין",
                phone = document.getString("טלפון איש קשר") ?: "לא זמין",
                parking = document.getString("חניה לרכבים") ?: "לא זמין",
                lighting = document.getString("תאורה קיימת") ?: "לא ידוע"
            )
            field
        } catch (e: Exception) {
            null
        }
    }
}