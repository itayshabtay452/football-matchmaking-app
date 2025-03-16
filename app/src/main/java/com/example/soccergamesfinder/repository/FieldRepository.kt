package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.Field
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
    private var lastDocument: DocumentSnapshot? = null
    private val batchSize = 20

    suspend fun getFields(reset: Boolean = false): List<Field> {
        return try {

            if (reset) lastDocument = null

            var query = firestore.collection("facilities")
                .orderBy("שם המתקן")
                .limit(batchSize.toLong())

            if (lastDocument != null && !reset) {
                query = query.startAfter(lastDocument!!)
            }

            val result = query.get().await()

            if (result.documents.isNotEmpty()) lastDocument = result.documents.last()

            result.documents.map { doc ->
                Field(
                    id = doc.getString("id") ?: "",
                    name = doc.getString("שם המתקן") ?: "",
                    size = doc.getString("גודל") ?: "",
                    fencing = doc.getString("גידור קיים") ?: "",
                    email = doc.getString("דוא\"ל איש קשר") ?: "",
                    phone = doc.getString("טלפון איש קשר") ?: "",
                    parking = doc.getString("חניה לרכבים") ?: "",
                    address = doc.getString("כתובת") ?: "",
                    lighting = doc.getString("תאורה קיימת") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
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

    fun resetPagination() {
        lastDocument = null
    }
}