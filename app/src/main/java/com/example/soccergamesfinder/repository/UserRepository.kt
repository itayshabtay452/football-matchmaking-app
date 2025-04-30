package com.example.soccergamesfinder.repository

import android.content.Context
import android.net.Uri
import com.example.soccergamesfinder.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Responsible for retrieving user data from Firestore.
 */
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context // 👈 הוספה

) {
    suspend fun hasUserData(): Boolean {
        val uid = firebaseAuth.currentUser?.uid ?: return false
        val document = firestore.collection("users").document(uid).get().await()
        return document.exists()
    }

    fun listenToUserById(
        userId: String,
        onChange: (User?) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                onChange(user)
            }
    }


    /**
     * Creates a new user document in Firestore with optional profile image.
     * @return Result<Unit> indicating success or failure.
     */
    suspend fun createUser(
        fullName: String,
        nickname: String,
        latitude: Double,
        longitude: Double,
        profileImageUri: Uri?
    ): Result<Unit> {
        val userId = firebaseAuth.currentUser?.uid
            ?: return Result.failure(Exception("User is not authenticated"))

        val imageUrl = profileImageUri?.let {
            uploadProfileImage(it, userId)
        }

        val user = User(
            id = userId,
            fullName = fullName,
            nickname = nickname,
            latitude = latitude,
            longitude = longitude,
            profileImageUrl = imageUrl
        )

        return try {
            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    suspend fun getUserById(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Uploads a profile image to Firebase Storage and returns its download URL.
     */
    private suspend fun uploadProfileImage(uri: Uri, userId: String): String? {
        println("Uploading image from URI userrepository upload: $uri") // DEBUG
        val filename = UUID.randomUUID().toString()
        val ref = firebaseStorage.reference.child("profile_pictures/$userId/$filename")

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return null // אם לא הצליח לפתוח את הקובץ

            ref.putStream(inputStream).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Checks if a nickname is already in use by another user.
     */
    suspend fun isNicknameTaken(nickname: String, ): Boolean {
        return try {
            val result = firestore.collection("users")
                .whereEqualTo("nickname", nickname)
                .get()
                .await()

            !result.isEmpty
        } catch (e: Exception) {
            false // אם יש שגיאה נניח שהכינוי פנוי כדי לא לחסום
        }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<User> {
        return try {
            val chunks = userIds.chunked(10) // מחלק את הרשימה לחלקים של עד 10 מזהים
            val users = mutableListOf<User>()

            for (chunk in chunks) {
                val snapshot = firestore.collection("users")
                    .whereIn("id", chunk)
                    .get()
                    .await()

                users += snapshot.toObjects(User::class.java)
            }

            users
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun followField(userId: String, fieldId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("fieldsFollowed", FieldValue.arrayUnion(fieldId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unfollowField(userId: String, fieldId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("fieldsFollowed", FieldValue.arrayRemove(fieldId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followGame(userId: String, gameId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("gamesFollowed", FieldValue.arrayUnion(gameId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unfollowGame(userId: String, gameId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("gamesFollowed", FieldValue.arrayRemove(gameId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeGameFromAllUsers(gameId: String): Result<Unit> {
        return try {
            val usersSnapshot = firestore.collection("users")
                .whereArrayContains("gamesFollowed", gameId)
                .get()
                .await()

            val batch = firestore.batch()

            for (doc in usersSnapshot.documents) {
                val userRef = doc.reference
                batch.update(userRef, "gamesFollowed", FieldValue.arrayRemove(gameId))
            }

            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersFollowingField(fieldId: String): List<String> {
        return try {
            val snapshot = firestore.collection("users")
                .whereArrayContains("fieldsFollowed", fieldId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersFollowingGame(gameId: String): List<String> {
        return try {
            val snapshot = firestore.collection("users")
                .whereArrayContains("gamesFollowed", gameId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }


    fun signOut() {
        firebaseAuth.signOut()
    }



}