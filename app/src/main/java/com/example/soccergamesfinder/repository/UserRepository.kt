package com.example.soccergamesfinder.repository

import com.example.soccergamesfinder.data.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun createUser(user: User) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        firestore.collection("users").document(userId).set(user).await()
    }

    suspend fun getUser(): User? {
        val userId = auth.currentUser?.uid ?: return null
        val document = firestore.collection("users").document(userId).get().await()
        return User(
            name = document.getString("name") ?: "",
            nickname = document.getString("nickname") ?: "",
            age = document.getLong("age")?.toInt() ?: 0,
            city = document.getString("city") ?: ""
        )
    }

    suspend fun hasUserData(): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        val document = firestore.collection("users").document(userId).get().await()
        return document.exists()
    }

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }
}