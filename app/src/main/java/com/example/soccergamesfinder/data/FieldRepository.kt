package com.example.soccergamesfinder.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FieldRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("fields")

    fun uploadField(field: FieldModel, onComplete: (Boolean) -> Unit) {
        val doc = collection.document()  // יוצר ID אוטומטי חדש
        val fieldWithId = field.copy(id = doc.id)

        doc.set(fieldWithId)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    suspend fun getAllFields(): List<FieldModel> {
        return firestore.collection("fields")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(FieldModel::class.java)?.copy(id = doc.id)
            }
    }
}