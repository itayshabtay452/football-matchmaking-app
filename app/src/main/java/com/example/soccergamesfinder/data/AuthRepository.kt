package com.example.soccergamesfinder.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    // פונקציה לאימות (Sign In)
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // פונקציה לרישום משתמש (Register) – רק אימייל וסיסמה
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // פונקציה להשלמת פרופיל המשתמש – עדכון שאר הפרטים במסד הנתונים (Firestore)
    suspend fun completeProfile(
        fullName: String,
        location: String,
        travelDistance: String,
        freeHours: String,
        skillLevel: String,
        imageUri: String
    ): Result<Unit> {
        return try {
            // קבלת ה-UID של המשתמש הנוכחי
            val uid = auth.currentUser?.uid ?: throw Exception("משתמש לא זמין")
            // בניית מפת הנתונים לשמירה במסד הנתונים
            val userData = hashMapOf(
                "fullName" to fullName,
                "location" to location,
                "travelDistance" to travelDistance,
                "freeHours" to freeHours,
                "skillLevel" to skillLevel,
                "imageUri" to imageUri
            )
            // כתיבה למסמך במסד הנתונים באוסף "users" תחת המסמך שמסומן ב-UID של המשתמש
            firestore.collection("users").document(uid).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // פונקציה לבדיקה אם המשתמש כבר השלים את הפרופיל
    suspend fun isProfileComplete(): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.exists() &&
                doc.getString("fullName")?.isNotEmpty() == true &&
                doc.getString("location")?.isNotEmpty() == true
    }
}
