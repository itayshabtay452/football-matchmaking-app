package com.example.soccergamesfinder.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    suspend fun getUserDisplayName(userId: String): String {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            snapshot.getString("nickname") ?: "לא ידוע"
        } catch (e: Exception) {
            "שגיאה בטעינה"
        }
    }
}
