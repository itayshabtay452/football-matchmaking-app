package com.example.soccergamesfinder.repository

import android.net.Uri
import com.example.soccergamesfinder.data.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun createUser(user: User, imageUri: Uri?) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val imageUrl = imageUri?.let { uploadProfileImage(userId, it) }
        val userWithImage = user.copy(profileImageUrl = imageUrl)
        firestore.collection("users").document(userId).set(userWithImage).await()

        }

    suspend fun getUser(): User? {
        val userId = auth.currentUser?.uid ?: return null
        val document = firestore.collection("users").document(userId).get().await()
        val user = document.toObject(User::class.java)
        return user
    }

    suspend fun hasUserData(): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        val document = firestore.collection("users").document(userId).get().await()
        return document.exists()
    }

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    private suspend fun uploadProfileImage(userId: String, imageUri: Uri): String? {
        return try {
            val ref = storage.reference.child("profile_pictures/$userId/profile.jpg")
            ref.putFile(imageUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserById(userId: String): User? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}