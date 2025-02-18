package com.example.soccergamesfinder.data

import com.example.soccergamesfinder.data.SoccerField
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getSoccerFields(): List<SoccerField> {
        return try {
            val snapshot = db.collection("soccer_fields").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(SoccerField::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
