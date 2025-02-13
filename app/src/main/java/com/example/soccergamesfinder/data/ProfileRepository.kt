package com.example.soccergamesfinder.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun isNicknameTaken(nickName: String): Boolean {
        if (nickName.isBlank()) {
            throw IllegalArgumentException("Nickname cannot be empty")
        }
        val querySnapshot = db.collection("users")
            .whereEqualTo("nickName", nickName)
            .get()
            .await()
        return !querySnapshot.isEmpty
    }

    suspend fun completeProfile(data: Map<String, Any>) {
        // משתמשים ב־UID של המשתמש הנוכחי כ־ID למסמך
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("משתמש לא זמין")
        db.collection("users")
            .document(uid)
            .set(data)
            .await()
    }
}
