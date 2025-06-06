package com.example.soccergamesfinder.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Handles authentication-related operations using Firebase Authentication.
 *
 * Supports:
 * - Email/password login and registration
 * - Google Sign-In using ID token
 *
 * Exposes suspend functions that return [Result<Unit>] to indicate success or failure.
 *
 * @constructor Injected via Hilt with an instance of [FirebaseAuth]
 */
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    /**
     * Attempts to log in a user with email and password.
     *
     * @param email User's email
     * @param password User's password
     * @return [Result.success] on success, [Result.failure] with exception on error
     */
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registers a new user with email and password.
     *
     * @param email New user's email
     * @param password New user's password
     * @return [Result.success] on success, [Result.failure] with exception on error
     */
    suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs in a user using a Google ID token.
     *
     * @param idToken Google ID token retrieved from GoogleSignInClient
     * @return [Result.success] on success, [Result.failure] with exception on error
     */
    suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
